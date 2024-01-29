package org.digit.exchange;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({TracerConfiguration.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = { "org.digit.exchange", "org.digit.exchange.controllers", "org.digit.exchange.config" })
public class DigitExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitExchangeApplication.class, args);
	}

}
