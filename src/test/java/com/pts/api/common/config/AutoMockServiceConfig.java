package com.pts.api.common.config;

import java.util.Arrays;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class AutoMockServiceConfig implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
        throws BeansException {
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(beanName -> {
            BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
            String beanClassName = bd.getBeanClassName();
            if (beanClassName != null) {
                try {
                    Class<?> clazz = Class.forName(beanClassName);
                    if (clazz.isAnnotationPresent(org.springframework.stereotype.Service.class)) {
                        Object mock = Mockito.mock(clazz);
                        beanFactory.registerSingleton(beanName, mock);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
