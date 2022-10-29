package org.egov.ifix.consumer;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.RequestHeader.RequestHeaderBuilder;
import org.egov.ifix.mapper.EventMapper;
import org.egov.ifix.models.FiscalEvent;
import org.egov.ifix.models.FiscalEventRequest;
import org.egov.ifix.models.FiscalEventResponse;
import org.egov.ifix.persistance.EventPostingDetail;
import org.egov.ifix.persistance.EventPostingDetailRepository;
import org.egov.ifix.service.PostEvent;
import org.egov.ifix.service.ProjectService;
import org.egov.ifix.utils.MasterDataMappingUtil;
import org.egov.ifix.utils.RequestHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventTypeConsumer {

	private static final String EVENT_TYPE = "eventType";

	private static final String EVENT = "event";

	private List<EventMapper> eventMappers;

	private Map<String, EventMapper> eventTypeMap = new HashMap<>();

	private PostEvent postEvent;

	private EventPostingDetailRepository eventPostingDetailRepository;

	private RequestHeaderUtil requestHeaderUtil;

	private ProjectService projectService;

	@Autowired
	public EventTypeConsumer(List<EventMapper> eventMappers, PostEvent postEvent,
			EventPostingDetailRepository eventPostingDetailRepository, RequestHeaderUtil requestHeaderUtil,
			ProjectService projectService) {
		this.eventMappers = Collections.unmodifiableList(eventMappers);
		this.postEvent = postEvent;
		this.eventPostingDetailRepository = eventPostingDetailRepository;
		this.requestHeaderUtil = requestHeaderUtil;
		this.projectService = projectService;
		initializeEventTypeMap();

	}

	private void initializeEventTypeMap() {
		for (EventMapper eventMapper : eventMappers)
			eventTypeMap.put(eventMapper.getEventType(), eventMapper);
	}

	@KafkaListener(topics = "${kafka.topics.ifix.adaptor.mapper}")
	public void listen(final String record) {

		process(record, null);
	}

	public void process(final String record, EventPostingDetail detail) {
		log.info("Received Message from topic: " + record);
		JsonObject jsonObject = null;
		log.info("\n\n");
		try {
			jsonObject = JsonParser.parseString(record).getAsJsonObject();
			EventMapper eventMapper = eventTypeMap.get(jsonObject.getAsJsonObject(EVENT).get(EVENT_TYPE).getAsString());
			List<FiscalEvent> fiscalEvents = eventMapper.transformData(jsonObject);
			FiscalEventRequest request = new FiscalEventRequest();
			RequestHeader header = new RequestHeader();
			header = requestHeaderUtil.polulateRequestHeader(jsonObject, header);
			request.setRequestHeader(header);

			loop: for (FiscalEvent event : fiscalEvents) {

				if (jsonObject.getAsJsonObject(EVENT).get("projectId") != null) {
					log.info("mgram prooject id " + jsonObject.getAsJsonObject(EVENT).get("projectId"));
					String clientCode = jsonObject.getAsJsonObject(EVENT).get("projectId").getAsString();
					String iFixprojectId = projectService.getProjectId(clientCode, jsonObject);
					event.setProjectId(iFixprojectId);

				}
				request.setFiscalEvent(event);
				log.info("Project Id" + event.getProjectId());

				if (detail == null) {
					detail = new EventPostingDetail();
					if (jsonObject.getAsJsonObject(EVENT).get("id") != null) {
						detail.setEventId(jsonObject.getAsJsonObject(EVENT).get("id").getAsString());
						detail.setTenantId(event.getTenantId());
						detail.setReferenceId(event.getParentReferenceId());
						detail.setEventType(event.getEventType());
						detail.setCreatedDate(new Date());
						detail.setLastModifiedDate(new Date());

					}
				}

				try {

					ResponseEntity<FiscalEventResponse> response = postEvent.post(request);

					if (response.getStatusCode().series().equals(HttpStatus.Series.SERVER_ERROR)
							|| response.getStatusCode().series().equals(HttpStatus.Series.CLIENT_ERROR)) {
						detail.setRecord(record);
					} else if (response.getStatusCode().series().equals(HttpStatus.Series.SUCCESSFUL)) {

						detail.setIfixEventId(response.getBody().getFiscalEvent().get(0).getId());
					}
					detail.setStatus(String.valueOf(response.getStatusCode().value()));
					detail.setRecord(record);
					eventPostingDetailRepository.save(detail);

				} catch (ResourceAccessException e) {
					log.info(e.getMessage(), e);
					detail.setStatus("500");
					detail.setError(extractedMessage(e));
					detail.setRecord(record);
					eventPostingDetailRepository.save(detail);
					break loop;
				}

				catch (RestClientException e) {
					log.info(e.getMessage(), e);
					detail.setStatus("400");
					detail.setError(extractedMessage(e));
					detail.setRecord(record);
					eventPostingDetailRepository.save(detail);

				}

			}
		} catch (RuntimeException e) {
			log.info(e.getMessage(), e);

			log.info(e.getMessage(), e);
			EventPostingDetail errorDetail = new EventPostingDetail();
			errorDetail.setEventId(jsonObject.getAsJsonObject(EVENT).get("id").getAsString());
			errorDetail.setTenantId(jsonObject.getAsJsonObject(EVENT).get("tenantId").getAsString());
			errorDetail.setReferenceId("Not extracted");
			errorDetail.setEventType(jsonObject.getAsJsonObject(EVENT).get(EVENT_TYPE).getAsString());
			errorDetail.setCreatedDate(new Date());
			errorDetail.setLastModifiedDate(new Date());
			errorDetail.setStatus("400");
			errorDetail.setError(extractedMessage(e));
			errorDetail.setRecord(record);
			eventPostingDetailRepository.save(errorDetail);
		}

	}

	private String extractedMessage(Exception e) {
		String message = null;
		if (e.getMessage().length() > 4000) {
			message = e.getMessage().substring(0, 3999);
		} else {
			message = e.getMessage();
		}
		return message;
	}

}
