package org.egov.ifix.consumer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.ifix.exception.GenericCustomException;
import org.egov.ifix.mapper.EventMapper;
import org.egov.ifix.master.MasterDataEnricher;
import org.egov.ifix.models.FiscalEvent;
import org.egov.ifix.models.FiscalEventRequest;
import org.egov.ifix.models.FiscalEventResponse;
import org.egov.ifix.persistance.EventPostingDetail;
import org.egov.ifix.persistance.EventPostingDetailRepository;
import org.egov.ifix.service.PostEvent;
import org.egov.ifix.service.impl.ProjectServiceImpl;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.DataWrapper;
import org.egov.ifix.utils.RequestHeaderUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static org.egov.ifix.utils.EventConstants.*;


@Service
@Slf4j
public class EventTypeConsumer {

    @Autowired
    private Map<String, EventMapper> eventTypeMap;

    @Autowired
    private MasterDataEnricher masterDataEnricher;

    @Autowired
    private PostEvent postEvent;

    @Autowired
    private EventPostingDetailRepository eventPostingDetailRepository;

    @Autowired
    private RequestHeaderUtil requestHeaderUtil;

    @Autowired
    private ProjectServiceImpl projectService;

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private DataWrapper dataWrapper;

    @KafkaListener(topics = "${kafka.topics.ifix.adaptor.mapper}")
    public void listen(final String record) {
        processFiscalEvent(record);
    }

    /**
     * @param record
     */
    @Transactional
    private void processFiscalEvent(String record) {
        log.info(LOG_INFO_PREFIX + "Transforming and collecting fiscal event");

        validateEventRequest(record);
        FiscalEventRequest fiscalEventRequest = new FiscalEventRequest();


        JsonObject jsonObject = JsonParser.parseString(record).getAsJsonObject();
        JsonObject eventJsonObject = jsonObject.getAsJsonObject(EVENT);
        String eventId = eventJsonObject.get(ID).getAsString();

        try {
            RequestHeader header = requestHeaderUtil.populateRequestHeader(jsonObject, new RequestHeader());
            fiscalEventRequest.setRequestHeader(header);

            ObjectNode additionalAttributes = masterDataEnricher.getMasterDataForProjectCode(
                    eventJsonObject.get(PROJECT_ID).getAsString());

            EventMapper eventMapper = eventTypeMap.get(eventJsonObject.get(EVENT_TYPE).getAsString());

            List<FiscalEvent> fiscalEvents = eventMapper.transformData(jsonObject);
            fiscalEvents.forEach(fiscalEvent -> fiscalEvent.setAttributes(additionalAttributes));

            fiscalEventRequest.setFiscalEvent(fiscalEvents);

            postFiscalEvent(record, eventId, fiscalEventRequest,
                    eventJsonObject.get(EVENT_TYPE).getAsString());

        } catch (Exception e) {
            log.error(LOG_ERROR_PREFIX + NON_RECOVERABLE_ERROR +
                    "Exception while transforming and collecting fiscal data. ", e);

            EventMapper eventMapper = eventTypeMap.get(eventJsonObject.get(EVENT_TYPE).getAsString());
            List<String> referenceIdList = eventMapper.getReferenceIdList(jsonObject);
            List<EventPostingDetail> eventPostingDetailList = new ArrayList<>();

            EventPostingDetail errorDetail = new EventPostingDetail();
            errorDetail.setEventId(eventJsonObject.get(ID).getAsString());
            errorDetail.setTenantId(eventJsonObject.get(TENANT_ID).getAsString());
            errorDetail.setEventType(eventJsonObject.get(EVENT_TYPE).getAsString());
            errorDetail.setCreatedDate(new Date());
            errorDetail.setLastModifiedDate(new Date());
            errorDetail.setStatus(NA);
            errorDetail.setError("Internal Fiscal Event Conversion/Transformation error : " + e.getMessage());
            errorDetail.setRecord(record);

            for (String referenceId : referenceIdList) {
                EventPostingDetail cloneDetails = new EventPostingDetail();
                BeanUtils.copyProperties(errorDetail, cloneDetails);

                cloneDetails.setReferenceId(referenceId);
                eventPostingDetailRepository.save(cloneDetails);
            }
        }
    }


    /**
     * TODO: Only erroneous event should be captured and saved in DB (detail.setRecord(record)).
     * 	There is no segragation between successfull or unsuccessfull fiscal event in response.
     * 	Either whole fiscal event get fail or pass.
     *
     * @param record
     * @param eventId
     * @param fiscalEventRequest
     * @param eventPostingDetailList
     */
    public void postFiscalEvent(final String record, String eventId, FiscalEventRequest fiscalEventRequest,
                                String eventType) {

        List<EventPostingDetail> eventPostingDetailList = new ArrayList<>();

        if (fiscalEventRequest != null && fiscalEventRequest.getFiscalEvent() != null && !StringUtils.isEmpty(record)
                && !StringUtils.isEmpty(eventId)) {
            try {
                ResponseEntity<FiscalEventResponse> fiscalEventResponseEntity = postEvent.post(fiscalEventRequest);

                eventPostingDetailList = wrapEventResponse(fiscalEventResponseEntity,
                        fiscalEventRequest.getFiscalEvent(), eventId, record, eventType);

            } catch (RestClientException e) {
                log.error(LOG_ERROR_PREFIX + RECOVERABLE_ERROR + e.getMessage(), e);

                eventPostingDetailList = composeEventPostingDetail(fiscalEventRequest.getFiscalEvent(), eventId,
                        HttpStatus.BAD_REQUEST, record, e.getMessage());

//TODO: Error handling stream.
/*
                Optional<ErrorDataModel> errorDataModelOptional = dataWrapper.getErrorDataModel(record,
                        FISCAL_EVENT_DATA_NAME, EVENT_TYPE, eventType, RECOVERABLE_ERROR,
                        HttpStatus.BAD_REQUEST.toString(), e.getMessage());

                if (errorDataModelOptional.isPresent()) {
                    kafkaTemplate.send(applicationConfiguration.getErrorTopicName(), errorDataModelOptional.get());
                }
*/

            } catch (Exception e) {
                log.error(LOG_ERROR_PREFIX + NON_RECOVERABLE_ERROR + e.getMessage(), e);

                eventPostingDetailList = composeEventPostingDetail(fiscalEventRequest.getFiscalEvent(), eventId,
                        HttpStatus.INTERNAL_SERVER_ERROR, record, e.getMessage());
//TODO: Error handling stream.
/*
                Optional<ErrorDataModel> errorDataModelOptional = dataWrapper.getErrorDataModel(record,
                        FISCAL_EVENT_DATA_NAME, EVENT_TYPE, eventType, RECOVERABLE_ERROR,
                        HttpStatus.BAD_REQUEST.toString(), e.getMessage());

                if (errorDataModelOptional.isPresent()) {
                    kafkaTemplate.send(applicationConfiguration.getErrorTopicName(), errorDataModelOptional.get());
                }
*/
            }

            eventPostingDetailRepository.saveAll(eventPostingDetailList);
        } else {
            log.error(LOG_ERROR_PREFIX + NON_RECOVERABLE_ERROR
                    + "Invalid parameter to push event");
        }
    }

    /**
     * @param fiscalEventResponseEntity
     * @param fiscalEventList
     * @param eventId
     * @param record
     * @return
     */
    private List<EventPostingDetail> wrapEventResponse(ResponseEntity<FiscalEventResponse> fiscalEventResponseEntity,
                                                       List<FiscalEvent> fiscalEventList, String eventId, String record,
                                                       String eventType) {
        if (fiscalEventResponseEntity != null && fiscalEventResponseEntity.getStatusCode() != null
                && fiscalEventList != null && !StringUtils.isEmpty(eventId) && !StringUtils.isEmpty(record)) {
            List<EventPostingDetail> eventPostingDetailList = new ArrayList<>();

            if (HttpStatus.Series.SERVER_ERROR.equals(fiscalEventResponseEntity.getStatusCode().series())
                    || HttpStatus.Series.CLIENT_ERROR.equals(fiscalEventResponseEntity.getStatusCode().series())) {

                eventPostingDetailList = composeEventPostingDetail(fiscalEventList, eventId,
                        fiscalEventResponseEntity.getStatusCode(), record, NA);

                log.error(LOG_ERROR_PREFIX + RECOVERABLE_ERROR +
                        "4 or 5 Series exception while sending request to Fiscal Event Service");

//TODO: Error handling stream.
/*

                Optional<ErrorDataModel> errorDataModelOptional = dataWrapper.getErrorDataModel(record,
                        FISCAL_EVENT_DATA_NAME, EVENT_TYPE, eventType, RECOVERABLE_ERROR,
                        HttpStatus.BAD_REQUEST.toString(),
                        "4 or 5 Series exception while sending request to Fiscal Event Service");

                if (errorDataModelOptional.isPresent()) {
                    kafkaTemplate.send(applicationConfiguration.getErrorTopicName(), eventPostingDetailList);
                }
*/

            } else if (HttpStatus.Series.SUCCESSFUL.equals(fiscalEventResponseEntity.getStatusCode().series())) {
                List<FiscalEvent> respondedFiscalEvents = fiscalEventResponseEntity.getBody().getFiscalEvent();

                if (respondedFiscalEvents != null && !respondedFiscalEvents.isEmpty()) {
                    eventPostingDetailList = composeEventPostingDetail(respondedFiscalEvents, eventId,
                            fiscalEventResponseEntity.getStatusCode(), NA, NA);

                    log.info(LOG_INFO_PREFIX + "Succussfully send fiscal event to Fiscal Event Service");
                } else {

                    eventPostingDetailList = composeEventPostingDetail(fiscalEventList, eventId,
                            fiscalEventResponseEntity.getStatusCode(), record, EMPTY_FISCAL_EVENT);

                    log.error(LOG_ERROR_PREFIX + RECOVERABLE_ERROR +
                            "Unable to receive Fiscal Event list in response");
//TODO: Error handling stream.
/*

                    Optional<ErrorDataModel> errorDataModelOptional = dataWrapper.getErrorDataModel(record,
                            FISCAL_EVENT_DATA_NAME, EVENT_TYPE, eventType, RECOVERABLE_ERROR,
                            HttpStatus.BAD_REQUEST.toString(),
                            "Unable to receive Fiscal Event list in response");

                    if (errorDataModelOptional.isPresent()) {
                        kafkaTemplate.send(applicationConfiguration.getErrorTopicName(), eventPostingDetailList);
                    }
*/

                }
            }

            return eventPostingDetailList;
        }

        return Collections.emptyList();
    }


    /**
     * @param fiscalEventList
     * @param eventId
     * @param httpStatus
     * @param record
     * @return
     */
    private List<EventPostingDetail> composeEventPostingDetail(List<FiscalEvent> fiscalEventList, final String eventId,
                                                               final HttpStatus httpStatus, final String record,
                                                               final String error) {
        if (fiscalEventList != null && !StringUtils.isEmpty(eventId) && httpStatus != null) {
            return fiscalEventList.stream()
                    .map(fiscalEvent -> {
                        EventPostingDetail eventPostingDetail = new EventPostingDetail();
                        eventPostingDetail.setEventId(eventId);
                        eventPostingDetail.setTenantId(fiscalEvent.getTenantId());
                        eventPostingDetail.setReferenceId(fiscalEvent.getReferenceId());
                        eventPostingDetail.setEventType(fiscalEvent.getEventType());
                        eventPostingDetail.setCreatedDate(new Date());
                        eventPostingDetail.setLastModifiedDate(new Date());
                        eventPostingDetail.setProjectId(fiscalEvent.getProjectId());
                        eventPostingDetail.setError(error);
                        eventPostingDetail.setStatus(String.valueOf(httpStatus.value()));

                        if (HttpStatus.Series.SUCCESSFUL.equals(httpStatus.series())) {
                            eventPostingDetail.setIfixEventId(fiscalEvent.getId());
                        } else {
                            eventPostingDetail.setRecord(record);
                        }

                        return eventPostingDetail;
                    }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    /**
     * TODO: Message truncation by character limit - not confirmed.
     *
     * @param e
     * @return
     */
    private String extractedMessage(Exception e) {
        String message = null;
        if (e.getMessage().length() > 4000) {
            message = e.getMessage().substring(0, 3999);
        } else {
            message = e.getMessage();
        }
        return message;
    }


    /**
     * TODO: Validation of whole event request still pending
     *
     * @param eventRequest
     */
    private void validateEventRequest(String eventRequest) {
        if (eventRequest != null) {
            JsonObject jsonObject = JsonParser.parseString(eventRequest).getAsJsonObject();

            if (!jsonObject.has(EVENT)) {
                throw new GenericCustomException(EVENT, "Event attribute is missing in payload");
            } else {
                Map<String, String> errorMap = new HashMap<>();
                JsonObject eventObject = jsonObject.getAsJsonObject(EVENT);

                if (!eventObject.has(EVENT_TYPE)) {
                    errorMap.put(EVENT_TYPE, "Event Type is missing in payload");
                }

                if (!eventObject.has(PROJECT_ID)
                        || eventObject.get(PROJECT_ID).isJsonNull()) {
                    errorMap.put(PROJECT_ID, "Project id is missing in event payload");
                }

                if (!eventObject.has(ID)) {
                    errorMap.put(ID, "Id is missing in event payload");
                }

                if (!eventObject.has(ENTITY)
                        || eventObject.getAsJsonArray(ENTITY).size() <= 0) {
                    errorMap.put(ENTITY, "Event Entity is missing in payload");
                }

                if (!errorMap.isEmpty()) {
                    throw new GenericCustomException(errorMap);
                }
            }
        } else {
            throw new GenericCustomException(PAYLOAD, "Invalid payload");
        }
    }
}
