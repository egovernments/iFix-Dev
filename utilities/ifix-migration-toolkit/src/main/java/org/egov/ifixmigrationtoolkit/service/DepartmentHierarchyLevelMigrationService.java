package org.egov.ifixmigrationtoolkit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifixmigrationtoolkit.models.*;
import org.egov.ifixmigrationtoolkit.producer.Producer;
import org.egov.ifixmigrationtoolkit.repository.DepartmentEntityMigrationRepository;
import org.egov.ifixmigrationtoolkit.repository.ServiceRequestRepository;
import org.egov.ifixmigrationtoolkit.util.MigrationEnum;
import org.egov.ifixmigrationtoolkit.util.ObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Service
public class DepartmentHierarchyLevelMigrationService {

    @Autowired
    private DepartmentEntityMigrationRepository migrationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private Producer producer;

    @Autowired
    private ObjectWrapper objectWrapper;

    @Value("${department.hierarchy.migration.batch.size}")
    private Integer batchSize;

    @Value("${department.entity.migration.persister.topic}")
    private String persistMigrationDataTopic;

    @Value("${persister.kafka.department.hierarchy.create.topic}")
    private String persisterKafkaDepartmentHierarchyCreateTopic;

    @Value("${ifix.department.entity.service.host}")
    private String departmentEntityServiceHost;

    public Map<String, Object> migrateDepartmentHieraryLevelData(MigrationRequest migrationRequest) {
        Map<String, Object> responseMap = new HashMap<>();

        Integer resumeFrom = migrationRepository
                .getPageNumberToResumeFrom(migrationRequest.getTenantId(), MigrationEnum.DEPARTMENT_HIERARCHY_LEVEL);

        Long numberOfRecordsMigrated = migrationRepository
                .getTotalNumberOfRecordsMigrated(migrationRequest.getTenantId(), MigrationEnum.DEPARTMENT_HIERARCHY_LEVEL);

        PlainsearchCriteria criteria = PlainsearchCriteria.builder().tenantId(migrationRequest.getTenantId()).build();
        DepartmentPlainSearchRequest plainSearchRequest = DepartmentPlainSearchRequest.builder()
                .criteria(criteria).build();
        plainSearchRequest.setRequestHeader(migrationRequest.getRequestHeader());


        Optional<Long> totalNumberOfRecordsOptional = getTotalRecordCountOfDepartmentHierarchy(plainSearchRequest);

        if (!totalNumberOfRecordsOptional.isPresent()) {
            log.error("DHL_MIGRATION_ERR", "No record found to migrate");
            responseMap.put("DHL_MIGRATION_ERR", "No record found to migrate");

            return responseMap;
        }else {
            Long totalNumberOfRecords = totalNumberOfRecordsOptional.get();
            Long totalNumberOfRecordsMigrated = numberOfRecordsMigrated;

            while (Boolean.TRUE) {
                log.info("At page: " + resumeFrom);
                plainSearchRequest.getCriteria().setOffset(resumeFrom);
                plainSearchRequest.getCriteria().setLimit(batchSize);
                DepartmentHierarchyLevelResponse response = null;

                try {
                    Object plainSearchResponse = serviceRequestRepository
                            .fetchResult(
                                    new StringBuilder(departmentEntityServiceHost
                                            + "ifix-department-entity/departmentEntity/hierarchyLevel/v1/_plainsearch"),
                                    plainSearchRequest);

                    response = objectMapper.convertValue(plainSearchResponse, DepartmentHierarchyLevelResponse.class);

                    if(CollectionUtils.isEmpty(response.getDepartmentHierarchyLevel()))
                        break;

                }catch (Exception e){
                    log.error("DHL_MIGRATION_ERR", "Some error occurred while migrating department hierarchy level data");
                    responseMap.put("DHL_MIGRATION_ERR", "Error occurred while migrating department hierarchy level " +
                            "data on page index: " + resumeFrom);

                    responseMap.put("DHL_MIGRATION_INGO", "Number of records migrated: " + totalNumberOfRecordsMigrated);

                    // Commit page number to resume migration from in case of any error while migrating
                    commitMigrationProgress(migrationRequest.getTenantId(), resumeFrom, batchSize,
                            totalNumberOfRecordsMigrated);

                    return responseMap;
                }

                if(totalNumberOfRecordsMigrated < totalNumberOfRecords){
                    seekPointerToAvoidDuplication(response, totalNumberOfRecordsMigrated, batchSize);
                }

                totalNumberOfRecordsMigrated += response.getDepartmentHierarchyLevel().size();

                producer.push(persisterKafkaDepartmentHierarchyCreateTopic,
                        objectWrapper.wrapPersisterDepartmentHierarchyLevelRequest(response));

                commitMigrationProgress(migrationRequest.getTenantId(), resumeFrom, batchSize,
                        totalNumberOfRecordsMigrated);

                resumeFrom++;
            }

            log.info("DHL: Number of records migrated in current session: " + totalNumberOfRecordsMigrated);
            responseMap.put("DHL_MIGRATION_INGO", "Number of records migrated in current session: "
                    + totalNumberOfRecordsMigrated);

            if (totalNumberOfRecordsMigrated % batchSize == 0) {
                commitMigrationProgress(migrationRequest.getTenantId(), resumeFrom, batchSize, totalNumberOfRecordsMigrated);
            }

            return responseMap;
        }

    }

    /**
     * @param response
     * @param totalNumberOfRecordsMigrated
     * @param batchSize
     */
    private void seekPointerToAvoidDuplication(DepartmentHierarchyLevelResponse response,
                                               Long totalNumberOfRecordsMigrated, Integer batchSize) {

        List<DepartmentHierarchyLevel> departmentHierarchyLevelList = new ArrayList<>();
        Long pointer = totalNumberOfRecordsMigrated % batchSize;
        String stringPointer = String.valueOf(pointer);

        for(int i = Integer.valueOf(stringPointer); i < response.getDepartmentHierarchyLevel().size(); i++){
            departmentHierarchyLevelList.add(response.getDepartmentHierarchyLevel().get(i));
        }
        response.setDepartmentHierarchyLevel(departmentHierarchyLevelList);
        log.info("DHL: No of records to be migrated post seeking pointer - "
                + response.getDepartmentHierarchyLevel().size());
    }

    /**
     * @param tenantId
     * @param pageNumber
     * @param batchSize
     * @param totalNumberOfRecordsMigrated
     */
    private void commitMigrationProgress(String tenantId, Integer pageNumber, Integer batchSize,
                                         Long totalNumberOfRecordsMigrated){

        DepartmentEntityMigrationCount migrationCount = DepartmentEntityMigrationCount.builder()
                .id(UUID.randomUUID().toString())
                .createdTime(System.currentTimeMillis())
                .tenantId(tenantId)
                .pageNumber(pageNumber)
                .batchSize(batchSize)
                .totalNumberOfRecordsMigrated(totalNumberOfRecordsMigrated)
                .serviceType(MigrationEnum.DEPARTMENT_HIERARCHY_LEVEL.name())
                .build();

        producer.push(persistMigrationDataTopic, DepartmentEntityMigrationCountWrapper.builder()
                .migrationCount(migrationCount).build());
    }


    /**
     * @param plainSearchRequest
     * @return
     */
    private Optional<Long> getTotalRecordCountOfDepartmentHierarchy(@NonNull DepartmentPlainSearchRequest plainSearchRequest) {
        try {
            Object countResponse = serviceRequestRepository
                    .fetchResult(new StringBuilder(departmentEntityServiceHost
                                    + "ifix-department-entity/departmentEntity/hierarchyLevel/v1/_count"),
                            plainSearchRequest);

            if (countResponse != null) {
                DepartmentEntityCountResponse departmentEntityCountResponse = objectMapper
                        .convertValue(countResponse, DepartmentEntityCountResponse.class);

                if (departmentEntityCountResponse != null && departmentEntityCountResponse.getCount() != null) {
                    return Optional.ofNullable(departmentEntityCountResponse.getCount());
                }
            }
        } catch (Exception e) {
            log.error("DHL_MIGRATION_ERR", "Some error occurred while fetching record cound fro Department Entity", e);
        }

        return Optional.empty();
    }
}
