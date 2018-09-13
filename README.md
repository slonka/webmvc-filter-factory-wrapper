# webmvc-filter-factory-wrapper

## Background:

I'm developing a library that creates common filters for WebMvc (Filter class) and for WebFlux (WebFilter class).
I have adapters for both of these classes.
I would like to be able to only declare my custom filter class (MyFilter) as a Bean and then have a BeanDefinitionRegistryPostProcessor automatically wrap and create adapters for each class when needed (WebMvc or WebFlux depending on the user needs).
I was able to do this for WebFlux, but creating a BeanDefinitionRegistryPostProcessor for WebMvc seems to not work.

I can't use BeanFactoryPostProcessor and declare adapters as singletons because the initialization will happen before @Autowired fields and properties are injected and that will cause NPE.

## Code / Test

You can find a test case that proves that `MyFilterWebFluxBeanFactoryPostProcessor` successfully creates and register filters in `net.slonka.webmvcfilter.WebFluxFilterTest#shouldAutoWrapFiltersForWebFlux`. 

You can find a test case that proves that `MyFilterWebMvcBeanFactoryPostProcessor` *DOESN'T* register filters in `net.slonka.webmvcfilter.WebMvcFilterTest#shouldAutoWrapFiltersForWebMvc`.

## Solution

How Can I solve this issue for WebMvc?
