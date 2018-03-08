package com.xzchaoo.learn.spring.ext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ObjectUtils;

/**
 * 当需要在Spring上下文偷偷引入一些bean就可以用这种方式
 *
 * @author xzchaoo
 * @date 2017/12/29
 */
public class FooImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
	private ConfigurableListableBeanFactory beanFactory;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		//由于这里是用代码注册的方式, IDEA会认为你没有提供FooService的实现
		AnnotationAttributes aa = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableFoo.class.getName()));
		String[] ss = beanFactory.getBeanNamesForType(FooService.class, false, false);
		if (ObjectUtils.isEmpty(ss)) {
			//if (!registry.containsBeanDefinition("foo1")) {
			GenericBeanDefinition d = new GenericBeanDefinition();
			d.setBeanClass(FooServiceImpl.class);
			d.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			d.setSynthetic(true);
			String info = aa.getString("value");
			//d.setAttribute("info", info);
			d.getPropertyValues().add("info", info);
			registry.registerBeanDefinition("foo1", d);
			//}
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}
}