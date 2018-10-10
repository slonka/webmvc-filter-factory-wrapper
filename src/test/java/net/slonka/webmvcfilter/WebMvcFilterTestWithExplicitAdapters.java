package net.slonka.webmvcfilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = {RootController.class, WebmvcfilterApplication.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = "use_postprocessor: false"
)
@ContextConfiguration(classes = WebMvcFilterTestWithExplicitAdapters.FilterConfiguration.class)
public class WebMvcFilterTestWithExplicitAdapters {

	@Value("${local.server.port}")
	protected int port;

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	@Qualifier("myFirstFilter")
	MyFilter myFirstFilter;

	@Autowired
	@Qualifier("mySecondFilter")
	MyFilter mySecondFilter;

	@Autowired
	@Qualifier("myThirdFilter")
	MyFilter myThirdFilter;

	@Test
	public void shouldAutoWrapFiltersForWebMvc() {
		makeRequest();
		assert myFirstFilter.getExecutedAt() > 0;
		assert mySecondFilter.getExecutedAt() > 0;
		assert mySecondFilter.getExecutedAt() > myFirstFilter.getExecutedAt();
		assert myThirdFilter.getExecutedAt() > mySecondFilter.getExecutedAt();
	}

	public String url(String path) {
		return String.format("http://localhost:%s%s", port, path);
	}

	private void makeRequest() {
		HttpStatus response = testRestTemplate.getForEntity(url("/"), String.class).getStatusCode();
		assert response.is2xxSuccessful();
	}

	@Configuration
	static class FilterConfiguration {
		@Bean
		MyFilter mySecondFilter() {
			return new MyFilter();
		}

		@Bean
		MyFilter myThirdFilter() {
			return new LowPriorityFilter();
		}

		@Bean
		MyFilter myFirstFilter() {
			return new HighPriorityFilter();
		}

		// we do not want this
		@Bean
		MyFilterWebMvcAdapter myFirstFilterAdapter(@Qualifier("myFirstFilter") MyFilter myFirstFilter) {
			return new MyFilterWebMvcAdapter(myFirstFilter);
		}

		@Bean
		MyFilterWebMvcAdapter mySecondFilterAdapter(@Qualifier("mySecondFilter") MyFilter mySecondFilter) {
			return new MyFilterWebMvcAdapter(mySecondFilter);
		}

		@Bean
		MyFilterWebMvcAdapter myThirdFilterAdapter(@Qualifier("myThirdFilter") MyFilter myThirdFilter) {
			return new MyFilterWebMvcAdapter(myThirdFilter);
		}

	}
}
