package org.egov.ifixmigrationtoolkit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifixmigrationtoolkit.models.MigrationRequest;
import org.egov.ifixmigrationtoolkit.models.adaptermasterdata.DepartmentRequest;
import org.egov.ifixmigrationtoolkit.models.adaptermasterdata.DepartmentResponse;
import org.egov.ifixmigrationtoolkit.models.adaptermasterdata.DepartmentSearchCriteria;
import org.egov.ifixmigrationtoolkit.models.adaptermasterdata.DepartmentSearchRequest;
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
    }

    public void migrateProject(MigrationRequest migrationRequest) {
        log.info("Starting migration of project master data....");
    }

}
