package net.slonka.webmvcfilter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@ConditionalOnProperty(name = "use_postprocessor")
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
			String filterName = myFilter + "WebMvcFilter";
            BeanDefinition beanDefinition =
                    BeanDefinitionBuilder.rootBeanDefinition(MyFilterAdapterFactory.class)
                            .addConstructorArgReference(myFilter).addConstructorArgValue(filterName).getBeanDefinition();

            registry.registerBeanDefinition(filterName, beanDefinition);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    public static class MyFilterAdapterFactory extends FilterRegistrationBean<Filter> {

        public MyFilterAdapterFactory(MyFilter myFilter, String name) {
            super(new MyFilterWebMvcAdapter(myFilter));
            setName(name);
            setOrder(myFilter.getOrder());
        }
    }
}
