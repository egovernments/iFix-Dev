package org.egov.ifixespipeline.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
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

    @Value("${fiscal.event.es.push.topic}")
    private String indexFiscalEventsTopic;

    @Value("${fiscal.event.kafka.push.topic}")
    private String fiscalEventsNewRecordsTopic;

    @Value("${ifix.master.data.service.host}")
    private String ifixMasterDataServiceHost;

    @Value("${ifix.master.data.service.search.endpoint}")
    private String ifixMasterDataServiceSearchEndpoint;

    @Value("${coa.electricity.head.name}")
    private String electricityCoaHeadName;

    @Value("${coa.operations.head.name}")
    private String operationsCoaHeadName;

    @Value("${coa.salary.head.name}")
    private String salaryCoaHeadName;

    private Map<String, Map<String, HashSet<String>>> tenantIdVsExpenditureTypeVsUuidsMap = new HashMap<>();

    /**
     * Kafka consumer
     *
     * @param record
     * @param topic
     */
    @KafkaListener(topics = { "${fiscal.event.kafka.push.topic}", "${fiscal.event.migration.origin.push.topic}"})
    public void listen(HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            FiscalEventRequest incomingData = objectMapper.convertValue(record, FiscalEventRequest.class);

            // Enrich hierarchy map according to the tenantid encountered by this pipeline to avoid redundant network calls
            if(!tenantIdVsExpenditureTypeVsUuidsMap.containsKey(incomingData.getFiscalEvent().getTenantId()))
                tenantIdVsExpenditureTypeVsUuidsMap.put(incomingData.getFiscalEvent().getTenantId(), loadExpenditureTypeVsUuidMap(incomingData.getFiscalEvent().getTenantId()));

            fiscalDataEnrichmentService.enrichFiscalData(incomingData);
            fiscalDataEnrichmentService.enrichComputedFields(incomingData, tenantIdVsExpenditureTypeVsUuidsMap.get(incomingData.getFiscalEvent().getTenantId()));

            producer.push(indexFiscalEventsTopic, incomingData);
        }catch(Exception e) {
            log.error("Exception while reading from the queue: ", e);
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

        response.getChartOfAccounts().forEach(chartOfAccount -> {

            if(expenditureTypeVsUuidsMap.containsKey(electricityCoaHeadName))
                expenditureTypeVsUuidsMap.get(electricityCoaHeadName).add(chartOfAccount.getId());
            else if(expenditureTypeVsUuidsMap.containsKey(operationsCoaHeadName))
                expenditureTypeVsUuidsMap.get(operationsCoaHeadName).add(chartOfAccount.getId());
            else if(expenditureTypeVsUuidsMap.containsKey(salaryCoaHeadName))
                expenditureTypeVsUuidsMap.get(salaryCoaHeadName).add(chartOfAccount.getId());
            else
                expenditureTypeVsUuidsMap.get("Others").add(chartOfAccount.getId());

        });
        return expenditureTypeVsUuidsMap;
    }

    private StringBuilder getIfixMasterDataUri() {
        StringBuilder uri = new StringBuilder(ifixMasterDataServiceHost);
        uri.append(ifixMasterDataServiceSearchEndpoint);
        return uri;
    }
}
