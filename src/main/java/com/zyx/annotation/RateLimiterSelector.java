package com.zyx.annotation;

import com.zyx.limiter.RateLimiter;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Method;
import java.util.Map;

public class RateLimiterSelector implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> value = importingClassMetadata.getAnnotationAttributes("com.zyx.annotation.EnableRateLimit", true);
        Object cl = value.get("value");
        RootBeanDefinition beanDefinition = new RootBeanDefinition(cl.toString());
        beanDefinition.setTargetType(RateLimiter.class);
        beanDefinition.setAutowireCandidate(true);
        beanDefinition.setSynthetic(true);
        registry.registerBeanDefinition("rateLimiter", beanDefinition);
    }

    private ConfigurableListableBeanFactory beanFactory;

    // 实现BeanFactoryAware的方法，设置BeanFactory
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        }
    }
}
