package org.egov.ifixmigrationtoolkit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifixmigrationtoolkit.models.MigrationRequest;
import org.egov.ifixmigrationtoolkit.models.adaptermasterdata.*;
import org.egov.ifixmigrationtoolkit.models.adaptermasterdata.projectnew.*;
import org.egov.ifixmigrationtoolkit.producer.Producer;
import org.egov.ifixmigrationtoolkit.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        ProjectSearchCriteria projectSearchCriteria = ProjectSearchCriteria.builder()
                .tenantId(migrationRequest.getTenantId())
                .build();
        ProjectSearchRequest projectSearchRequest = ProjectSearchRequest.builder()
                .criteria(projectSearchCriteria)
                .requestHeader(migrationRequest.getRequestHeader())
                .build();
        Object response = serviceRequestRepository.fetchResult(
                new StringBuilder(adapterMasterDataServiceHost + "adapter-master-data/project/v1/_search"),
                projectSearchRequest);
        ProjectResponse projectResponse = objectMapper.convertValue(response, ProjectResponse.class);

        ProjectResponseNew projectResponseNew = transformProjects(projectResponse);

        for(ProjectDTONew project : projectResponseNew.getProjectDTO()) {
            producer.push("save-project-application",
                    ProjectRequestNew.builder().projectDTO(project).build());
        }
    }

    private ProjectResponseNew transformProjects(ProjectResponse projectResponse) {
        List<ProjectDTONew> newProjects = new ArrayList<>();
        for(Project project : projectResponse.getProject()) {

            List<ProjectDepartmentEntityRelationshipDTO> departmentEntityRelationshipDTOS = null;
            if(project.getDepartmentEntityIds() != null) {
                departmentEntityRelationshipDTOS = new ArrayList<>();
                for (String departmentEntityId : project.getDepartmentEntityIds()) {
                    departmentEntityRelationshipDTOS.add(
                            ProjectDepartmentEntityRelationshipDTO.builder()
                                    .departmentEntityId(departmentEntityId)
                                    .status(true)
                                    .build());
                }
            }

            List<ProjectLocationRelationshipDTO> locationRelationshipDTOS = null;
            if(project.getLocationIds() != null) {
                locationRelationshipDTOS = new ArrayList<>();
                for(String locationId : project.getLocationIds()) {
                    locationRelationshipDTOS.add(
                            ProjectLocationRelationshipDTO.builder()
                                    .locationId(locationId)
                                    .status(true)
                                    .build());
                }
            }

            ProjectDTONew newProject = ProjectDTONew.builder()
                    .id(project.getId())
                    .tenantId(project.getTenantId())
                    .name(project.getName())
                    .code(project.getCode())
                    .expenditureId(project.getExpenditureId())
                    .departmentEntityIds(departmentEntityRelationshipDTOS)
                    .locationIds(locationRelationshipDTOS)
                    .auditDetails(project.getAuditDetails())
                    .build();
        }
        return ProjectResponseNew.builder().projectDTO(newProjects).build();
    }

}
