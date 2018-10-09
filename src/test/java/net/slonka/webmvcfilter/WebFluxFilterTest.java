package net.slonka.webmvcfilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@Import(MyFilterWebFluxBeanFactoryPostProcessor.class)
@SpringBootTest(
		classes = {RootController.class, WebmvcfilterApplication.class},
		properties = "spring.main.web-application-type=reactive",
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WebFluxFilterTest {

    @Autowired
    MyFilter myFirstFilter;

    @Autowired
    MyFilter mySecondFilter;

	@Autowired
	WebTestClient webTestClient;

    @Test
    public void shouldAutoWrapFiltersForWebFlux() {
        makeRequest();
        assert myFirstFilter.getExecutedAt() > 0;
        assert mySecondFilter.getExecutedAt() > 0;
        assert mySecondFilter.getExecutedAt() > myFirstFilter.getExecutedAt();
    }

    private void makeRequest() {
        webTestClient.head().uri("/").exchange().expectStatus().isOk();
    }

	@Configuration
	static class FilterConfiguration {
		@Bean
		MyFilter myFirstFilter() {
			return new MyFilter();
		}

        @Bean
        MyFilter mySecondFilter() {
            return new MyFilter();
        }
	}
}
