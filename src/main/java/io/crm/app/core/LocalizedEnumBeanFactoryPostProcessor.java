package io.crm.app.core;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import io.crm.app.core.annotation.LocalizedLabel;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Configuration
@SuppressWarnings("rawtypes")
@Slf4j
public class LocalizedEnumBeanFactoryPostProcessor implements BeanFactoryPostProcessor, InitializingBean {
	
	private final List<Class<?>> enumClasses = Lists.newArrayList();
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		for (Class<?> enumClass : enumClasses) {
			if( enumClass.getAnnotation(LocalizedLabel.class) == null )
				continue;

			Enum[] enumValues = (Enum[])enumClass.getEnumConstants();
			if( ArrayUtils.isEmpty(enumValues))
				continue;
			for (Enum enumVal : enumValues) {
				log.info(enumClass.getName());
				BeanDefinition def = new AnnotatedGenericBeanDefinition(enumClass);
				def.setBeanClassName(enumClass.getName());
				def.setFactoryMethodName("valueOf");
				def.getConstructorArgumentValues().addGenericArgumentValue(enumVal.name());
				((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(enumClass.getName() + "." + enumVal.name(), def);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(LocalizedLabel.class));
		
		List<String> enumPaths = Lists.newArrayList(
				"io.crm.app.core.constant");
		
		enumPaths.stream().forEach(path -> {
			enumClasses.addAll(scanner.findCandidateComponents(path)
					.stream()
					.map( bean -> uncheckCall(() -> Class.forName(bean.getBeanClassName())))
					.collect(Collectors.toList()));
		});
		
	}
	
	private static <T> T uncheckCall(Callable<T> callable) {
		try {
			return callable.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
