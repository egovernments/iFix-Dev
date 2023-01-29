package org.egov.ifixmigrationtoolkit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifixmigrationtoolkit.models.MigrationRequest;
import org.egov.ifixmigrationtoolkit.models.adaptermasterdata.*;
import org.egov.ifixmigrationtoolkit.producer.Producer;
import org.egov.ifixmigrationtoolkit.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdapterMasterDataMigrationService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Producer producer;

    @Value("${adapter.master.data.service.host}")
    private String adapterMasterDataServiceHost;

    public void migrateDepartment(MigrationRequest migrationRequest) {
        log.info("Starting migration of department master data....");
        DepartmentSearchCriteria departmentSearchCriteria =
                DepartmentSearchCriteria.builder()
                        .tenantId(migrationRequest.getTenantId())
                        .build();
        DepartmentSearchRequest departmentSearchRequest =
                DepartmentSearchRequest.builder()
                        .criteria(departmentSearchCriteria)
                        .requestHeader(migrationRequest.getRequestHeader())
                        .build();
        Object response = serviceRequestRepository.fetchResult(
                 new StringBuilder(adapterMasterDataServiceHost + "adapter-master-data/department/v1/_search"),
                departmentSearchRequest);
        DepartmentResponse departmentResponse = objectMapper.convertValue(response, DepartmentResponse.class);
        departmentResponse.getDepartment().forEach(department -> {
            producer.push("save-department-application",
                    DepartmentRequest.builder().department(department).requestHeader(migrationRequest.getRequestHeader()).build());
        });
    }

    public void migrateExpenditure(MigrationRequest migrationRequest) {
        log.info("Starting migration of expenditure master data....");
        ExpenditureSearchCriteria expenditureSearchCriteria = ExpenditureSearchCriteria.builder()
                .tenantId(migrationRequest.getTenantId())
                .build();
        ExpenditureSearchRequest expenditureSearchRequest = ExpenditureSearchRequest.builder()
                .requestHeader(migrationRequest.getRequestHeader())
                .criteria(expenditureSearchCriteria)
                .build();
        Object response = serviceRequestRepository.fetchResult(
                new StringBuilder(adapterMasterDataServiceHost + "adapter-master-data/expenditure/v1/_search"),
                expenditureSearchRequest);
        ExpenditureResponse expenditureResponse = objectMapper.convertValue(response, ExpenditureResponse.class);
        expenditureResponse.getExpenditure().forEach(expenditure -> {
            producer.push("save-expenditure-application",
                    ExpenditureRequest.builder().expenditure(expenditure).requestHeader(migrationRequest.getRequestHeader()).build());
        });
    }

    public void migrateProject(MigrationRequest migrationRequest) {
        log.info("Starting migration of project master data....");
    }

}
