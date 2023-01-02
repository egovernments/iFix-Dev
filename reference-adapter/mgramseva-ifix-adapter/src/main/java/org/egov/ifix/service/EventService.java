package org.egov.ifix.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.models.Event;
import org.egov.ifix.models.EventRequest;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.utils.EventConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class EventService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;


    public void pushEvent(EventRequest eventRequest) {
    	validateAndEnrichEventRequest(eventRequest);

        log.info(EventConstants.LOG_INFO_PREFIX + "Send request on queue");

        kafkaTemplate.send(applicationConfiguration.getMapperTopicName(), eventRequest);
    }

	/**
	 * @param eventRequest
	 */
    private void validateAndEnrichEventRequest(EventRequest eventRequest) {
		Map<String, String> errorMap = new HashMap<>();

		if (eventRequest != null && eventRequest.getEvent() != null
				&& !StringUtils.isEmpty(applicationConfiguration.getMapperTopicName())) {
			Event event = eventRequest.getEvent();

			if (event.getEventType() == null){
				errorMap.put(EventConstants.EVENT_TYPE, "Invalid Event Type or it is missing in payload");
			}

			if (StringUtils.isEmpty(event.getProjectId())) {
				errorMap.put(EventConstants.PROJECT_ID, "Project id is missing in event payload");
			}

			if (StringUtils.isEmpty(event.getTenantId())) {
				errorMap.put(EventConstants.TENANT_ID, "Tenant id is missing in event payload");
			}

			if (event.getEntity() == null || event.getEntity().isEmpty()) {
				errorMap.put(EventConstants.ENTITY, "Event Entity is missing in payload");
			}
		}else {
			throw new HttpCustomException(EventConstants.REQUEST_EVENT, "Invalid event request", HttpStatus.BAD_REQUEST);
		}

		if (!errorMap.isEmpty()) {
			throw new HttpCustomException(errorMap, HttpStatus.BAD_REQUEST);
		}else {
			eventRequest.getEvent().setId(UUID.randomUUID().toString());
		}
	}


}
