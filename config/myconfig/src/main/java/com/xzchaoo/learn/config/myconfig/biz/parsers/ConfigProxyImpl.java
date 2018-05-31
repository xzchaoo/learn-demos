package com.xzchaoo.learn.config.myconfig.biz.parsers;

import com.xzchaoo.learn.config.myconfig.biz.Property;
import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.ConfigObserver;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class ConfigProxyImpl implements ConfigProxy, ConfigObserver {
  /**
   * TODO 分离出去
   * 存放默认的 parser
   */
  private static final Map<Class, Parser<?>> DEFAULT_PARSER;

  static {
    Map<Class, Parser<?>> defaultParserMap = new HashMap<>();
    defaultParserMap.put(String.class, Parsers.STRING);

    defaultParserMap.put(int.class, Parsers.INTEGER);
    defaultParserMap.put(Integer.class, Parsers.INTEGER);

    defaultParserMap.put(float.class, Parsers.FLOAT);
    defaultParserMap.put(Float.class, Parsers.FLOAT);

    defaultParserMap.put(double.class, Parsers.DOUBLE);
    defaultParserMap.put(Double.class, Parsers.DOUBLE);

    defaultParserMap.put(long.class, Parsers.LONG);
    defaultParserMap.put(Long.class, Parsers.LONG);

    defaultParserMap.put(boolean.class, Parsers.BOOLEAN);
    defaultParserMap.put(Boolean.class, Parsers.BOOLEAN);

    DEFAULT_PARSER = Collections.unmodifiableMap(defaultParserMap);
  }

  /**
   * 实际的配置源
   */
  private final Config config;

  /**
   * 缓存配置实例
   */
  private final ConcurrentHashMap<Class, ConfigWrapper> configInstanceCache = new ConcurrentHashMap<>();

  public ConfigProxyImpl(Config config) {
    this.config = config;
    config.subscribe(this);
  }

  /**
   * 解析一个property
   *
   * @param property
   * @param field
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private static PropertyConfig<Object> parseProperty(Property property, Field field) throws Exception {
    Class<?> fieldType = field.getType();
    Parser parser;
    Class<? extends Parser> parserClass = property.parser();
    ParseAsList parseAsList = field.getAnnotation(ParseAsList.class);
    if (parseAsList != null) {
      char separator = parseAsList.separator();
      Class<?> contentType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

    }
    if (parserClass == Parser.None.class) {
      // find default
      parser = DEFAULT_PARSER.get(fieldType);
      if (parser == null) {
        throw new IllegalArgumentException();
      }
    } else {
      // cache parser?
      parser = parserClass.newInstance();
    }
    return new PropertyConfig<Object>(property.value(), (Class) fieldType, parser, property.defaultValue());
  }

  /**
   * 某些加锁才能保证安全
   */
  private final Object lock = new Object();

  /**
   * 创建一个配置实例 返回的是单例
   * TODO 该个名字
   *
   * @param clazz
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getConfig(Class<T> clazz) {
    try {
      ConfigWrapper wrapper = configInstanceCache.get(clazz);
      if (wrapper == null) {
        synchronized (lock) {
          wrapper = configInstanceCache.get(clazz);
          if (wrapper == null) {
            wrapper = createConfigItem(clazz);
            setProperties(wrapper, config.getAsMap());
            configInstanceCache.put(clazz, wrapper);
          }
        }
      }
      return (T) wrapper.getConfigInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 创建配置包装
   *
   * @param clazz
   * @return
   * @throws Exception
   */
  private ConfigWrapper createConfigItem(Class<?> clazz) throws Exception {
    ConfigConfig configConfig = new ConfigConfig();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      Property property = field.getAnnotation(Property.class);
      if (property != null) {
        field.setAccessible(true);
        PropertyConfig<Object> pc = parseProperty(property, field);
        configConfig.addFieldProperty(field, pc);
      }
    }
    return new ConfigWrapper(clazz.newInstance(), configConfig);
  }

  @SuppressWarnings("unchecked")
  private void setProperties(ConfigWrapper wrapper, Map<String, String> configMap) throws IllegalAccessException {
    Object configInstance = wrapper.getConfigInstance();
    ConfigConfig configConfig = wrapper.getConfigConfig();
    for (Map.Entry<Field, PropertyConfig<?>> e : configConfig.getFieldPropertyConfigs().entrySet()) {
      Field field = e.getKey();
      PropertyConfig pc = e.getValue();
      // null empty check?
      String strValue = configMap.get(pc.getKey());
      if (strValue == null) {
        strValue = pc.getDefaultValue();
      }
      Object objectValue = pc.getParser().parse(pc.getClazz(), strValue);
      field.set(configInstance, objectValue);
    }
  }

  @Override
  public void onChange(Config ignore) {
    synchronized (lock) {
      // TODO add 和 onChange 冲突
      for (ConfigWrapper li : configInstanceCache.values()) {
        try {
          setProperties(li, config.getAsMap());
        } catch (Exception e) {
          // 单个失败 跳过
          // log
          e.printStackTrace();
        }
      }
    }
  }
}
