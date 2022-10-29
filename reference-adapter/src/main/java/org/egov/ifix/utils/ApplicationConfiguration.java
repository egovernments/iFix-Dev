package org.egov.ifix.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@Getter
@Setter
@ToString
public class ApplicationConfiguration {
	
	@Value("${kafka.topics.ifix.adaptor.mapper}")
	private String mapperTopicName;
	
	@Value("${state.goverment.code}")
	private String tenantId;
	
	@Value("${keycloak.credentials.clientid}")
	private String clientId;
	
	@Value("${keycloak.credentials.clientsecret}")
	private String clientSecret;
	
	@Value("${keycloak.credentials.granttype}")
	private String grantType;
	
	@Value("${keycloak.host}")
	private String keyCloakHost;
	
	@Value("${keycloak.token.url}")
	private String keyCloakAuthApi;
	
	@Value("${ifix.host}")
	private String ifixHost;
	
	@Value("${ifix.event.url}")
	private String ifixEventApi;
	
	@Value("${ifix.coa.search.url}")
	private String coaSearchApi;
	
	@Value("${ifix.project.search.url}")
	private String projectSearchApi;


}
