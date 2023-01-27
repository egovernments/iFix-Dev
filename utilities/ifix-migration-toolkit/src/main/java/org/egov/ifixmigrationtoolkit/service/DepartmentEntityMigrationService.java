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
public class DepartmentEntityMigrationService {

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

    @Value("${department.entity.migration.batch.size}")
    private Integer batchSize;

    @Value("${department.entity.migration.persister.topic}")
    private String persistMigrationDataTopic;

    @Value("${persister.kafka.department.entity.create.topic}")
    private String persisterKafkaDepartmentEntityCreateTopic;

    @Value("${ifix.department.entity.service.host}")
    private String departmentEntityServiceHost;

    public Map<String, Object> migrateDepartmentEntityData(MigrationRequest migrationRequest) {
        Map<String, Object> responseMap = new HashMap<>();

        Integer resumeFrom = migrationRepository
                .getPageNumberToResumeFrom(migrationRequest.getTenantId(), MigrationEnum.DEPARTMENT_ENTITY);

        Long numberOfRecordsMigrated = migrationRepository
                .getTotalNumberOfRecordsMigrated(migrationRequest.getTenantId(), MigrationEnum.DEPARTMENT_ENTITY);

        PlainsearchCriteria criteria = PlainsearchCriteria.builder().tenantId(migrationRequest.getTenantId()).build();
        DepartmentPlainSearchRequest plainSearchRequest = DepartmentPlainSearchRequest.builder()
                .criteria(criteria).build();
        plainSearchRequest.setRequestHeader(migrationRequest.getRequestHeader());


        Optional<Long> totalNumberOfRecordsOptional = getTotalRecordCountOfDepartmentEntity(plainSearchRequest);

        if (!totalNumberOfRecordsOptional.isPresent()) {
            log.error("DE_MIGRATION_ERR", "No record found to migrate");
            responseMap.put("DE_MIGRATION_ERR", "No record found to migrate");

            return responseMap;
        }else {
            Long totalNumberOfRecords = totalNumberOfRecordsOptional.get();
            Long totalNumberOfRecordsMigrated = numberOfRecordsMigrated;

            while (Boolean.TRUE) {
                log.info("At page: " + resumeFrom);
                plainSearchRequest.getCriteria().setOffset(resumeFrom);
                plainSearchRequest.getCriteria().setLimit(batchSize);
                DepartmentEntityResponse response = null;

                try {
                    Object plainSearchResponse = serviceRequestRepository
                            .fetchResult(
                                    new StringBuilder(departmentEntityServiceHost + "ifix-department-entity/departmentEntity/v1/_plainsearch"),
                                    plainSearchRequest);

                    response = objectMapper.convertValue(plainSearchResponse, DepartmentEntityResponse.class);

                    if(CollectionUtils.isEmpty(response.getDepartmentEntity()))
                        break;

                }catch (Exception e){
                    log.error("DE_MIGRATION_ERR", "Some error occurred while migrating department entity data");
                    responseMap.put("DE_MIGRATION_ERR", "Error occurred while migrating department data on page index: "
                            + resumeFrom);

                    responseMap.put("DE_MIGRATION_INFO", "Number of records migrated: " + totalNumberOfRecordsMigrated);

                    // Commit page number to resume migration from in case of any error while migrating
                    commitMigrationProgress(migrationRequest.getTenantId(), resumeFrom, batchSize,
                            totalNumberOfRecordsMigrated);

                    return responseMap;
                }

                if(totalNumberOfRecordsMigrated < totalNumberOfRecords){
                    seekPointerToAvoidDuplication(response, totalNumberOfRecordsMigrated, batchSize);
                }

                totalNumberOfRecordsMigrated += response.getDepartmentEntity().size();

                publishToKafkaTopic(response);
//                producer.push(persisterKafkaDepartmentEntityCreateTopic,
//                        objectWrapper.wrapPersisterDepartmentEntityRequest(response));

                commitMigrationProgress(migrationRequest.getTenantId(), resumeFrom, batchSize,
                        totalNumberOfRecordsMigrated);

                resumeFrom++;
            }

            log.info("DE: Number of records migrated in current session: " + totalNumberOfRecordsMigrated);
            responseMap.put("DE_MIGRATION_INFO", "Number of records migrated in current session: "
                    + totalNumberOfRecordsMigrated);

            if (totalNumberOfRecordsMigrated % batchSize == 0) {
                commitMigrationProgress(migrationRequest.getTenantId(), resumeFrom, batchSize, totalNumberOfRecordsMigrated);
            }

            return responseMap;
        }
    }

    /**
     * @param response
     */
    private void publishToKafkaTopic(@NonNull DepartmentEntityResponse response) {
        List<PersisterDepartmentEntityRequest> departmentEntityRequestList =
                objectWrapper.wrapPersisterDepartmentEntityRequest(response);

        departmentEntityRequestList.stream()
                        .forEach(persisterDepartmentEntityRequest ->
                            producer.push(persisterKafkaDepartmentEntityCreateTopic, persisterDepartmentEntityRequest));
    }

    /**
     * @param response
     * @param totalNumberOfRecordsMigrated
     * @param batchSize
     */
    private void seekPointerToAvoidDuplication(DepartmentEntityResponse response, Long totalNumberOfRecordsMigrated,
                                               Integer batchSize) {

        List<DepartmentEntity> departmentEntityList = new ArrayList<>();
        Long pointer = totalNumberOfRecordsMigrated % batchSize;
        String stringPointer = String.valueOf(pointer);

        for(int i = Integer.valueOf(stringPointer); i < response.getDepartmentEntity().size(); i++){
            departmentEntityList.add(response.getDepartmentEntity().get(i));
        }
        response.setDepartmentEntity(departmentEntityList);
        log.info("DE: No of records to be migrated post seeking pointer - " + response.getDepartmentEntity().size());
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
                .serviceType(MigrationEnum.DEPARTMENT_ENTITY.name())
                .build();

        producer.push(persistMigrationDataTopic, DepartmentEntityMigrationCountWrapper.builder()
                .migrationCount(migrationCount).build());
    }


    /**
     * @param migrationRequest
     * @return
     */
    private Optional<Long> getTotalRecordCountOfDepartmentEntity(@NonNull DepartmentPlainSearchRequest plainSearchRequest) {
        try {
            Object countResponse = serviceRequestRepository
                    .fetchResult(new StringBuilder(departmentEntityServiceHost + "ifix-department-entity/departmentEntity/v1/_count"),
                            plainSearchRequest);

            if (countResponse != null) {
                DepartmentEntityCountResponse departmentEntityCountResponse = objectMapper
                        .convertValue(countResponse, DepartmentEntityCountResponse.class);

                if (departmentEntityCountResponse != null && departmentEntityCountResponse.getCount() != null) {
                    return Optional.ofNullable(departmentEntityCountResponse.getCount());
                }
            }
        } catch (Exception e) {
            log.error("DE_MIGRATION_ERR", "Some error occurred while fetching record cound fro Department Entity", e);
        }

        return Optional.empty();
    }

}
