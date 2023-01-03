package org.egov.ifix;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Value("${event.config.path}")
	private String configFilePath;

	@Autowired
	private ResourceLoader resourceLoader;

	@PostConstruct
	public void readConfig() throws IOException {

		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
		Resource resource = resourceLoader.getResource(configFilePath);

		Map<Object, Object> map = objectMapper.readValue(resource.getInputStream(), Map.class);

		System.out.println(map.toString());

	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
