package org.egov.ifixmigrationtoolkit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifixmigrationtoolkit.models.*;
import org.egov.ifixmigrationtoolkit.producer.Producer;
import org.egov.ifixmigrationtoolkit.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MasterDataMigrationService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Producer producer;

    @Value("${ifix.master.data.service.host}")
    private String ifixMasterDataServiceHost;

    public void migrateMasterData(MigrationRequest request){
        log.info("Starting migration of coa master data....");
        COASearchCriteria criteria = COASearchCriteria.builder().tenantId(request.getTenantId()).build();
        COASearchRequest searchRequest = COASearchRequest.builder().criteria(criteria).requestHeader(request.getRequestHeader()).build();
        Object coaSearchResponse = serviceRequestRepository.fetchResult(new StringBuilder(ifixMasterDataServiceHost + "ifix-master-data/chartOfAccount/v1/_search"), searchRequest);
        COAResponse response = objectMapper.convertValue(coaSearchResponse, COAResponse.class);
        response.getChartOfAccounts().forEach(chartOfAccount -> {
            producer.push("save-coa", COARequest.builder().chartOfAccount(chartOfAccount).requestHeader(request.getRequestHeader()).build());
        });
    }
}
