package org.digit.exchange.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digit.exchange.filters.pre.SignValidator;
import org.digit.exchange.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private SecurityService securityService;

    @Bean
    public FilterRegistrationBean<SignValidator> customFilterRegistration() {
        FilterRegistrationBean<SignValidator> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SignValidator(objectMapper, appConfig, securityService)); // Inject ObjectMapper into SignValidator constructor
        registration.addUrlPatterns("/v1/exchange/*");
        return registration;
    }
}