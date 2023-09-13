package org.egov.ifixespipeline.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.ifixespipeline.models.*;
import org.egov.ifixespipeline.producer.Producer;
import org.egov.ifixespipeline.repository.ServiceRequestRepository;
import org.egov.ifixespipeline.service.FiscalDataEnrichmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class IfixElasticSearchPipelineListener {

    @Autowired
    private FiscalDataEnrichmentService fiscalDataEnrichmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private Producer producer;

    @Value("${fiscal.event.index.topic}")
    private String indexFiscalEventsTopic;

    @Value("${fiscal.event.kafka.push.topic}")
    private String fiscalEventsNewRecordsTopic;

    @Value("${ifix.master.data.service.host}")
    private String ifixMasterDataServiceHost;

    @Value("${ifix.coa.search.endpoint}")
    private String ifixCoaSearchEndpoint;

    @Value("${coa.electricity.head.name}")
    private String electricityCoaHeadName;

    @Value("${coa.operations.head.name}")
    private String operationsCoaHeadName;

    @Value("${coa.salary.head.name}")
    private String salaryCoaHeadName;

    @Value("#{${coa.map}}")
    Map<String, HashSet<String>> coaMap;

    private Map<String, Map<String, HashSet<String>>> tenantIdVsExpenditureTypeVsUuidsMap = new HashMap<>();

    /**
     * Kafka consumer
     *
     * @param record
     * @param topic
     */
    @KafkaListener(topics = { "${fiscal.event.kafka.push.topic}" })
    public void listen(HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            FiscalEventBulkRequest fiscalEventBulkRequest = objectMapper.convertValue(record, FiscalEventBulkRequest.class);
            log.info("coaMap:" +coaMap);
            for(FiscalEvent fiscalEvent : fiscalEventBulkRequest.getFiscalEvent()) {
                // Enrich hierarchy map according to the tenantid encountered by this pipeline to avoid redundant network calls
                if(!tenantIdVsExpenditureTypeVsUuidsMap.containsKey(fiscalEvent.getTenantId()))
                    tenantIdVsExpenditureTypeVsUuidsMap.put(fiscalEvent.getTenantId(), coaMap);
                log.info("tenantIdVsExpenditureTypeVsUuidsMap:"+tenantIdVsExpenditureTypeVsUuidsMap);
                fiscalDataEnrichmentService.enrichFiscalData(fiscalEvent);
                fiscalDataEnrichmentService.enrichComputedFields(fiscalEvent,
                        tenantIdVsExpenditureTypeVsUuidsMap.get(fiscalEvent.getTenantId()));

                log.info("Before Pushing to indexer:" + fiscalEvent.toString());
                producer.push(indexFiscalEventsTopic, FiscalEventRequest.builder().fiscalEvent(fiscalEvent).build());
            }
        }catch(Exception e) {
            log.error("Exception while reading from the queue: ", e);
            throw new RuntimeException(e);
        }
    }

    private HashMap<String, HashSet<String>> loadExpenditureTypeVsUuidMap(String tenantId){
        COASearchCriteria criteria = COASearchCriteria.builder().tenantId(tenantId).build();
        COASearchRequest request = COASearchRequest.builder().requestHeader(new RequestHeader()).criteria(criteria).build();
        Object result = serviceRequestRepository.fetchResult(getIfixMasterDataUri(), request);
        COAResponse response = objectMapper.convertValue(result, COAResponse.class);
        HashMap<String, HashSet<String>> expenditureTypeVsUuidsMap = new HashMap<>();
        expenditureTypeVsUuidsMap.put("Others", new HashSet<>());
        expenditureTypeVsUuidsMap.put(electricityCoaHeadName, new HashSet<>());
        expenditureTypeVsUuidsMap.put(operationsCoaHeadName, new HashSet<>());
        expenditureTypeVsUuidsMap.put(salaryCoaHeadName, new HashSet<>());
        response.getChartOfAccounts().forEach(chartOfAccount -> {

            if(chartOfAccount.getObjectHeadName().equals(electricityCoaHeadName)) {
                expenditureTypeVsUuidsMap.get(electricityCoaHeadName).add(chartOfAccount.getId());
            }
            else if(chartOfAccount.getObjectHeadName().equals(operationsCoaHeadName)) {
                expenditureTypeVsUuidsMap.get(operationsCoaHeadName).add(chartOfAccount.getId());
            }
            else if(chartOfAccount.getObjectHeadName().equals(salaryCoaHeadName)) {
                expenditureTypeVsUuidsMap.get(salaryCoaHeadName).add(chartOfAccount.getId());
            }
            else {
                expenditureTypeVsUuidsMap.get("Others").add(chartOfAccount.getId());
            }

        });
        return expenditureTypeVsUuidsMap;
    }

    private StringBuilder getIfixMasterDataUri() {
        StringBuilder uri = new StringBuilder(ifixMasterDataServiceHost);
        uri.append(ifixCoaSearchEndpoint);
        return uri;
    }
}
