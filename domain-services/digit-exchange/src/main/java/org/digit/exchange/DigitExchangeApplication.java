package org.digit.exchange;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TracerConfiguration.class})
@ComponentScan(basePackages = { "org.digit.exchange", "org.digit.exchange.controller", "org.digit.exchange.config" })
public class DigitExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitExchangeApplication.class, args);
	}

}
