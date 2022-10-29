package org.egov.ifix.service;

import java.util.UUID;

import org.egov.ifix.models.EventRequest;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService {
	 
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	private ApplicationConfiguration applicationConfiguration;


	 
	
	@Autowired
	public EventService(KafkaTemplate<String, Object> kafkaTemplate, 
			ApplicationConfiguration applicationConfiguration
			) {

		this.kafkaTemplate = kafkaTemplate;
		this.applicationConfiguration = applicationConfiguration;
	}
	

	public void pushEvent(EventRequest eventRequest) {

		eventRequest.getEvent().setId(UUID.randomUUID().toString());
		log.info("Send request on queue");
		this.kafkaTemplate.send(applicationConfiguration.getMapperTopicName(), eventRequest);

	}

	public KafkaTemplate<String, Object> getKafkaTemplate() {
		return kafkaTemplate;
	}


	public void setKafkaTemplate(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}


	public ApplicationConfiguration getApplicationConfiguration() {
		return applicationConfiguration;
	}


	public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
		this.applicationConfiguration = applicationConfiguration;
	}


}
