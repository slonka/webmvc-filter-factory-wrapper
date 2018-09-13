package net.slonka.webmvcfilter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class MyFilterWebFluxBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        String[] myFilters = BeanFactoryUtils
                .beanNamesForTypeIncludingAncestors((ListableBeanFactory) this.beanFactory,
                        MyFilter.class);

        for (String myFilter : myFilters) {
            BeanDefinition beanDefinition =
                    BeanDefinitionBuilder.rootBeanDefinition(MyFilterAdapterFactory.class)
                            .addConstructorArgReference(myFilter).getBeanDefinition();

            registry.registerBeanDefinition(myFilter + "WebFluxFilter", beanDefinition);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    public static class MyFilterAdapterFactory
            implements FactoryBean<WebFilter> {

        MyFilter myFilter;

        public MyFilterAdapterFactory(MyFilter myFilter) {
            this.myFilter = myFilter;
        }

        @Override
        public WebFilter getObject() {
            return new MyFilterWebFluxAdapter(myFilter);
        }

        @Override
        public Class<?> getObjectType() {
            return WebFilter.class;
        }
    }
}
