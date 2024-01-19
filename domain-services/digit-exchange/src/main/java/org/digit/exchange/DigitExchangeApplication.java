package org.digit.exchange;

import org.digit.exchange.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
@ComponentScan(basePackages = { "org.digit.exchange" })
public class DigitExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitExchangeApplication.class, args);
	}

}
