package org.egov.ifixmigrationtoolkit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifixmigrationtoolkit.models.*;
import org.egov.ifixmigrationtoolkit.producer.Producer;
import org.egov.ifixmigrationtoolkit.repository.MigrationRepository;
import org.egov.ifixmigrationtoolkit.repository.ServiceRequestRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Service
public class MigrationService {

    @Autowired
    private MigrationRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private Producer producer;

    @Value("${ifix.migration.batch.size}")
    private Integer batchSize;

    @Value("${fiscal.event.es.topic}")
    private String fiscalEventESPushTopic;

    @Value("${ifix.migration.postgres.push.topic}")
    private String postgresSinkPushTopic;

    @Value("${ifix.migration.progress.topic}")
    private String saveMigrationProgressTopic;

    @Value("${ifix.fiscal.event.service.host}")
    private String ifixFiscalEventServiceHost;

    public Map<String, Object>  migrateData(MigrationRequest request){
        Integer resumeFrom = repository.getPageNumberToResumeFrom(request.getTenantId());
        Long numberOfRecordsMigrated = repository.getTotalNumberOfRecordsMigrated(request.getTenantId());
        PlainsearchCriteria criteria = PlainsearchCriteria.builder().tenantId(request.getTenantId()).build();
        FiscalEventPlainSearchRequest plainSearchRequest = FiscalEventPlainSearchRequest.builder().criteria(criteria).build();
        plainSearchRequest.setRequestHeader(request.getRequestHeader());
        Object countResponse = serviceRequestRepository.fetchResult(new StringBuilder(ifixFiscalEventServiceHost + "fiscal-event-service/events/v1/_count"), plainSearchRequest);
        FiscalEventCountResponse finalCountResponse = objectMapper.convertValue(countResponse, FiscalEventCountResponse.class);
        Long totalNumberOfRecords = finalCountResponse.getCount();
        Integer lastPageNumber = resumeFrom;
        Long totalNumberOfRecordsMigrated = numberOfRecordsMigrated;
        Map<String, Object> responseMap = new HashMap<>();
        int i = resumeFrom;
        while (Boolean.TRUE) {
            log.info("At page: " + i);
            plainSearchRequest.getCriteria().setOffset(i);
            plainSearchRequest.getCriteria().setLimit(batchSize);
            FiscalEventResponse response = null;
            try {
                Object plainSearchResponse = serviceRequestRepository.fetchResult(new StringBuilder(ifixFiscalEventServiceHost + "fiscal-event-service/events/v1/_plainsearch"), plainSearchRequest);
                response = objectMapper.convertValue(plainSearchResponse, FiscalEventResponse.class);

                if(CollectionUtils.isEmpty(response.getFiscalEvent()))
                    break;
            }catch (Exception e){
                log.error("IFIX_MIGRATION_ERR", "Some error occurred while migrating data");
                responseMap.put("IFIX_MIGRATION_ERR", "Error occurred while migrating data on page index: " + i);
                responseMap.put("IFIX_MIGRATION_INFO", "Number of records migrated: " + totalNumberOfRecordsMigrated);
                // Commit page number to resume migration from in case of any error while migrating
                commitMigrationProgress(request.getTenantId(), i, batchSize, totalNumberOfRecordsMigrated);
                return responseMap;
            }
            if(i == resumeFrom && totalNumberOfRecordsMigrated < totalNumberOfRecords){
                seekPointerToAvoidDuplication(response, totalNumberOfRecordsMigrated, batchSize);
            }
            totalNumberOfRecordsMigrated += response.getFiscalEvent().size();

            // send fiscal events to ES-Pipeline
            for(FiscalEvent fiscalEvent : response.getFiscalEvent()) {
                producer.push(fiscalEventESPushTopic, FiscalEventRequest.builder().fiscalEvent(fiscalEvent).build());
            }

            // Send fiscal events to postgres sink
            producer.push(postgresSinkPushTopic, prepareFiscalEventDTOListForPersister(response.getFiscalEvent()));
            commitMigrationProgress(request.getTenantId(), i, batchSize, totalNumberOfRecordsMigrated);
            i += 1;
            lastPageNumber = i;
        }
        log.info("Number of records migrated in current session: " + totalNumberOfRecordsMigrated);
        responseMap.put("IFIX_MIGRATION_INFO", "Number of records migrated in current session: " + totalNumberOfRecordsMigrated);
        commitMigrationProgress(request.getTenantId(), lastPageNumber, batchSize, totalNumberOfRecordsMigrated);
        return responseMap;
    }

    private void seekPointerToAvoidDuplication(FiscalEventResponse response, Long totalNumberOfRecordsMigrated, Integer batchSize) {
        List<FiscalEvent> listOfFiscalEvents = new ArrayList<>();
        Long pointer = totalNumberOfRecordsMigrated % batchSize;
        String stringPointer = String.valueOf(pointer);
        for(int i = Integer.valueOf(stringPointer); i < response.getFiscalEvent().size(); i++){
            listOfFiscalEvents.add(response.getFiscalEvent().get(i));
        }
        response.setFiscalEvent(listOfFiscalEvents);
        log.info("No of records to be migrated post seeking pointer - " + response.getFiscalEvent().size());
    }

    private void commitMigrationProgress(String tenantId, Integer pageNumber, Integer batchSize, Long totalNumberOfRecordsMigrated){
        MigrationCount migrationCount = MigrationCount.builder()
                .id(UUID.randomUUID().toString())
                .createdTime(System.currentTimeMillis())
                .tenantId(tenantId)
                .pageNumber(pageNumber)
                .batchSize(batchSize)
                .totalNumberOfRecordsMigrated(totalNumberOfRecordsMigrated)
                .build();
        //producer.push(saveMigrationProgressTopic, MigrationCountWrapper.builder().migrationCount(migrationCount).build());

    }

    public FiscalEventRequestDTO prepareFiscalEventDTOListForPersister(List<FiscalEvent> fiscalEventList) {
        List<FiscalEventDTO> listOfFiscalEvents = new ArrayList<>();
        FiscalEventRequestDTO requestDTO = new FiscalEventRequestDTO();


        fiscalEventList.forEach(fiscalEvent -> {
            FiscalEventDTO fiscalEventDTO = new FiscalEventDTO();
            BeanUtils.copyProperties(fiscalEvent, fiscalEventDTO);
            List<ReceiverDTO> receiverList = new ArrayList<>();
            fiscalEvent.getReceivers().forEach(receiver -> {
                ReceiverDTO receiverDTO = new ReceiverDTO();
                receiverDTO.setId(UUID.randomUUID().toString());
                receiverDTO.setFiscalEventId(fiscalEvent.getId());
                receiverDTO.setReceiver(receiver);
                receiverList.add(receiverDTO);
            });
            fiscalEventDTO.setReceivers(receiverList);
            fiscalEventDTO.getAmountDetails().forEach(amount -> {
                amount.setAuditDetails(fiscalEvent.getAuditDetails());
            });
            listOfFiscalEvents.add(fiscalEventDTO);
        });

        requestDTO.setFiscalEvent(listOfFiscalEvents);

        return requestDTO;
    }
}
