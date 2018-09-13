package net.slonka.webmvcfilter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class MyFilterWebMvcBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor, BeanFactoryAware {
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

            registry.registerBeanDefinition(myFilter + "WebMvcFilter", beanDefinition);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    public static class MyFilterAdapterFactory
            implements FactoryBean<Filter> {

        MyFilter myFilter;

        public MyFilterAdapterFactory(MyFilter myFilter) {
            this.myFilter = myFilter;
        }

        @Override
        public Filter getObject() {
            return new MyFilterWebMvcAdapter(myFilter);
        }

        @Override
        public Class<?> getObjectType() {
            return Filter.class;
        }
    }
}
