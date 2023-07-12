package com.cc.kingdeestart.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author seven up
 * @date 2023年07月10日 2:29 PM
 */
public class ApiClientRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware,
        ResourceLoaderAware, BeanFactoryAware {

    private Environment environment;

    private ResourceLoader resourceLoader;

    private BeanFactory beanFactory;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        KingDeeProperty kingDeeProperty = buildProperty();
        registerApiClient(importingClassMetadata, kingDeeProperty);
//        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

    private KingDeeProperty buildProperty() {
        KingDeeProperty property = new KingDeeProperty();
        property.setAcctId(environment.getProperty("kingdee.acctId"));
        property.setUserName(environment.getProperty("kingdee.userName"));
        property.setPassword(environment.getProperty("kingdee.password"));
        property.setLcId(environment.getProperty("kingdee.lcId"));
        property.setServerUrl(environment.getProperty("kingdee.serverUrl"));
        return property;
    }

    private void registerApiClient(AnnotationMetadata importingClassMetadata, KingDeeProperty kingDeeProperty) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(KingDeeApiClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);
//        String basePackage = getBasePackage(importingClassMetadata);
        String basePackage = "com.cc.kingdeestart.core";
        Set<BeanDefinition> beanDefinitionSet = scanner.findCandidateComponents(basePackage);
        beanDefinitionSet.forEach(candidateComponent -> {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                Assert.isTrue(annotationMetadata.isInterface(), "@KingDeeApiClient can only be specified on an interface");

                String clientName = getClientName(annotationMetadata);

                Class<?> originalClass;
                try {
                    originalClass = Class.forName(beanDefinition.getBeanClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Object proxy = Proxy.newProxyInstance(originalClass.getClassLoader(), new Class[]{originalClass},
                        new RequestInvocationHandler(kingDeeProperty));
                ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(clientName, proxy);
            }
        });
    }


    private static class RequestInvocationHandler implements InvocationHandler {
        private final RequestResolver requestResolver;

        public RequestInvocationHandler(KingDeeProperty kingDeeProperty) {
            this.requestResolver = new RequestResolver(kingDeeProperty);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            String methodName = method.getName();
            return requestResolver.invoke(methodName, args);
        }
    }


    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

//    protected String getBasePackage(AnnotationMetadata importingClassMetadata) {
//        Map<String, Object> attributes = importingClassMetadata
//                .getAnnotationAttributes(EnableKingDeeApiClient.class.getCanonicalName());
//        return (String) attributes.get("basePackage");
//    }

    private String getClientName(AnnotationMetadata metadata) {
        Map<String, Object> attributes = metadata
                .getAnnotationAttributes(KingDeeApiClient.class.getCanonicalName());
        return (String) attributes.get("name");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
