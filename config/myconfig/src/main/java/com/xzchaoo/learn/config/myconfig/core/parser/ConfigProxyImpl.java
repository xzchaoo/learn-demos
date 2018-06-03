package com.xzchaoo.learn.config.myconfig.core.parser;


import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.annotation.Property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class ConfigProxyImpl implements ConfigProxy {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProxyImpl.class);

  private final ParserProvider parserProvider;

  /**
   * 实际的配置源
   */
  private final Config config;

  /**
   * 用锁保护某些必要的操作
   */
  private final Object lock = new Object();

  /**
   * 缓存配置实例
   */
  private final ConcurrentHashMap<Class, PojoWrapper> configInstanceCache = new ConcurrentHashMap<>();

  ConfigProxyImpl(Config config, ParserProvider parserProvider) {
    this.config = config;
    this.parserProvider = parserProvider;
    config.addListener(this::onConfigChange);
  }

  /**
   * 创建一个配置实例 返回的是单例
   *
   * @param clazz
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getConfig(Class<T> clazz) {
    PojoWrapper wrapper = configInstanceCache.get(clazz);
    if (wrapper == null) {
      synchronized (lock) {
        wrapper = configInstanceCache.get(clazz);
        if (wrapper == null) {
          wrapper = createConfigItem(clazz);
          setPojoProperties(wrapper, config.asMap());
          configInstanceCache.put(clazz, wrapper);
        }
      }
    }
    return (T) wrapper.getPojo();
  }

  /**
   * 当配置发生变化时的回调
   *
   * @param config 配置对象
   */
  private void onConfigChange(Config config) {
    // 这里需要通过锁来保证 更新时有序的
    synchronized (lock) {
      Map<String, String> configMap = config.asMap();
      for (PojoWrapper wrapper : configInstanceCache.values()) {
        try {
          setPojoProperties(wrapper, configMap);
        } catch (Exception e) {
          // 单个config更新失败不影响其它配置
          LOGGER.error("fail to update config instance {}", wrapper.getPojo(), e);
        }
      }
    }
  }

  /**
   * 解析一个property
   *
   * @param property
   * @param field
   * @return
   */
  @SuppressWarnings("unchecked")
  private PropertyDescription<?> parseProperty(Property property, Field field) {
    Parser<?> parser = determineFieldParser(property, field);
    String defaultValue = property.defaultValue().equals(Property.NONE) ? null : property.defaultValue();
    return new PropertyDescription<>(property.value(), field, parser, defaultValue);
  }

  /**
   * 决定该属性需要使用的Parser
   *
   * @param property
   * @param field
   * @return
   */
  private Parser<?> determineFieldParser(Property property, Field field) {
    Class<?> fieldType = field.getType();
    // 不打算支持其他类型(包括子接口) 因此这里直接用 == 来判断了
    // 通常很少在代码里直接使用 Collection 接口的
    if (fieldType == List.class || fieldType == Set.class) {
      Class<?> valueType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
      Parser<?> valueParser = findParser(valueType, property.valueParser());
      if (fieldType == List.class) {
        return listParser(property.separator(), valueParser);
      } else {
        return setParser(property.separator(), valueParser);
      }
    } else if (fieldType == Map.class) {
      Type[] args = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
      Class<?> keyType = (Class<?>) args[0];
      Class<?> valueType = (Class<?>) args[1];
      Parser keyParser = findParser(keyType, property.keyParser());
      Parser valueParser = findParser(valueType, property.valueParser());
      return mapParser(property.separator(), property.separator2(), keyParser, valueParser);
    } else {
      return findParser(fieldType, property.valueParser());
    }
  }

  /**
   * 寻找Parser
   *
   * @param clazz              类型
   * @param defaultParserClass 默认的解析器实现类
   * @return
   */
  @SuppressWarnings("unchecked")
  private Parser<?> findParser(Class<?> clazz, Class<? extends Parser> defaultParserClass) {
    Parser<?> parser = parserProvider.getParser(clazz, (Class) defaultParserClass);
    if (parser == null) {
      throw new IllegalArgumentException("fail to find parser for " + clazz);
    }
    return parser;
  }

  /**
   * 创建配置包装
   *
   * @param clazz
   * @return
   */
  private PojoWrapper createConfigItem(Class<?> clazz) {
    // TODO 需要考虑继承关系么? 如果要的话 这里就需要做遍历了
    Map<Field, PropertyDescription<?>> fieldMap = new HashMap<>();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      // 只处理有Property注解的属性
      Property property = field.getAnnotation(Property.class);
      if (property != null) {
        field.setAccessible(true);
        fieldMap.put(field, parseProperty(property, field));
      }
    }
    return new PojoWrapper(createInstance(clazz), clazz, fieldMap);
  }


  /**
   * 根据给定的配置, 设置pojo的属性
   *
   * @param wrapper   pojo的包装
   * @param configMap 配置map
   */
  @SuppressWarnings("unchecked")
  private static void setPojoProperties(PojoWrapper wrapper, Map<String, String> configMap) {
    Object pojo = wrapper.getPojo();
    for (Map.Entry<Field, PropertyDescription<?>> e : wrapper.getFieldPropertyConfigs().entrySet()) {
      Field field = e.getKey();
      PropertyDescription pc = e.getValue();
      String strValue = configMap.get(pc.getKey());
      if (strValue == null && pc.getDefaultValue() != null) {
        strValue = pc.getDefaultValue();
      }
      try {
        Object value = pc.getParser().parse(strValue);
        field.set(pojo, value);
      } catch (IllegalAccessException ex) {
        LOGGER.warn("fail to set field {} for pojo {}", field, pojo, ex);
      }
    }
  }

  private static <T> T createInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("fail to create instance for class " + clazz);
    }
  }

  private static <T> Parser<List<T>> listParser(char separator, Parser<T> valueParser) {
    return collectionParser(separator, valueParser, ArrayList::new);
  }

  private static <T> Parser<Set<T>> setParser(char separator, Parser<T> valueParser) {
    return collectionParser(separator, valueParser, HashSet::new);
  }

  private static <T, C extends Collection<? super T>> Parser<C> collectionParser(char separator, Parser<T>
    valueParser, Supplier<C> collectionSupplier) {
    return new CollectionParser<>(separator, valueParser, collectionSupplier);
  }

  private static <K, V> MapParser<K, V> mapParser(char separator, char separator2, Parser<K> keyParser, Parser<V>
    valueParser) {
    return new MapParser<>(separator, separator2, keyParser, valueParser);
  }

}
