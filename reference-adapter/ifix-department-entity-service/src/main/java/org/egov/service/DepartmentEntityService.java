package org.egov.service;

import org.egov.common.contract.AuditDetails;
import org.egov.config.IfixDepartmentEntityConfig;
import org.egov.repository.DepartmentEntityRelationshipRepository;
import org.egov.repository.DepartmentEntityRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityAncestryUtil;
import org.egov.util.DtoWrapper;
import org.egov.util.KafkaProducer;
import org.egov.validator.DepartmentEntityValidator;
import org.egov.web.models.*;
import org.egov.web.models.persist.DepartmentEntity;
import org.egov.web.models.persist.DepartmentEntityRelationship;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentEntityService {

    @Autowired
    DepartmentEntityValidator departmentEntityValidator;

    @Autowired
    DepartmentEntityEnrichmentService entityEnrichmentService;

    @Autowired
    private DepartmentEntityRepository departmentEntityRepository;

    @Autowired
    private DepartmentEntityRelationshipRepository entityRelationshipRepository;

    @Autowired
    private IfixDepartmentEntityConfig ifixDepartmentEntityConfig;

    @Autowired
    private DepartmentEntityAncestryUtil departmentEntityAncestryUtil;

    @Autowired
    private DtoWrapper dtoWrapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    /**
     * @param departmentEntityRequest
     * @return
     */
    public DepartmentEntityRequest createDepartmentEntity(DepartmentEntityRequest departmentEntityRequest) {
        departmentEntityValidator.validateDepartmentEntityRequest(departmentEntityRequest);
        entityEnrichmentService.enrichDepartmentEntityData(departmentEntityRequest);

        kafkaProducer.push(ifixDepartmentEntityConfig.getPersisterKafkaDepartmentEntityCreateTopic(),
                departmentEntityRequest);

        return departmentEntityRequest;
    }

    public List<? extends DepartmentEntityAbstract> findAllByCriteria(DepartmentEntitySearchRequest departmentEntitySearchRequest) {
        departmentEntityValidator.validateSearchDepartmentEntity(departmentEntitySearchRequest);

        List<DepartmentEntity> departmentEntityList = departmentEntityRepository
                .getDepartmentEntitiesByParamsExistence(departmentEntitySearchRequest.getCriteria());

        List<DepartmentEntityDTO> departmentEntityDTOList = dtoWrapper.wrapDepartmentEntityListIntoDTOs(departmentEntityList);

        if (departmentEntitySearchRequest.getCriteria().isGetAncestry()) {
            List<DepartmentEntityAncestry> departmentEntityAncestryList = new ArrayList<>();

            for (DepartmentEntityDTO departmentEntityDTO : departmentEntityDTOList) {
                departmentEntityAncestryList.add(createAncestryFor(departmentEntityDTO));
            }
            return departmentEntityAncestryList;
        }
        return departmentEntityDTOList;
    }


    public DepartmentEntityAncestry createAncestryFor(@NotNull DepartmentEntityDTO departmentEntityDTO) {
        int hierarchyCount = ifixDepartmentEntityConfig.getMaximumSupportedDepartmentHierarchy();

        DepartmentEntityAncestry ancestry =
                departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity(departmentEntityDTO);

        while (hierarchyCount >= 0) {
            Optional<DepartmentEntity> parentEntityOptional = departmentEntityRepository.getParent(ancestry.getId());

            if (!parentEntityOptional.isPresent())
                break;

            DepartmentEntityDTO parentEntity = dtoWrapper.wrapDepartmentEntityIntoDTO(parentEntityOptional.get());

            DepartmentEntityAncestry parentAncestry =
                    departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity(parentEntity);
            parentAncestry.getChildren().add(ancestry);

            ancestry = parentAncestry;
            hierarchyCount--;
        }

        if (hierarchyCount < 0)
            throw new CustomException("MAXIMUM_SUPPORTED_HIERARCHY_EXCEEDED", "Loop to find ancestors " +
                    "exceeded the maximum supported hierarchy. The data might be corrupted.");

        return ancestry;
    }

    /**
     * @param departmentEntityRequest
     * @return
     */
    public DepartmentEntityRequest updateDepartmentEntity(DepartmentEntityRequest departmentEntityRequest) {
        boolean isModified = false;
        departmentEntityValidator.validateUpdateDepartmentEntityRequest(departmentEntityRequest);

        DepartmentEntityDTO requestedDepartmentEntityDTO = departmentEntityRequest.getDepartmentEntityDTO();

        Optional<DepartmentEntity> departmentEntityOptional = departmentEntityRepository
                .findById(requestedDepartmentEntityDTO.getId());

        if (departmentEntityOptional.isPresent()) {
            DepartmentEntity existingDepartmentEntity = departmentEntityOptional.get();

            if (!StringUtils.isEmpty(requestedDepartmentEntityDTO.getTenantId())) {
                existingDepartmentEntity.setTenantId(requestedDepartmentEntityDTO.getTenantId());
                isModified = true;
            }

            if (!StringUtils.isEmpty(requestedDepartmentEntityDTO.getDepartmentId())) {
                existingDepartmentEntity.setDepartmentId(requestedDepartmentEntityDTO.getDepartmentId());
                isModified = true;
            }

            if (!StringUtils.isEmpty(requestedDepartmentEntityDTO.getCode())) {
                existingDepartmentEntity.setCode(requestedDepartmentEntityDTO.getCode());
                isModified = true;
            }

            if (!StringUtils.isEmpty(requestedDepartmentEntityDTO.getName())) {
                existingDepartmentEntity.setName(requestedDepartmentEntityDTO.getName());
                isModified = true;
            }

            if (requestedDepartmentEntityDTO.getHierarchyLevel() != null) {
                existingDepartmentEntity.setHierarchyLevel(requestedDepartmentEntityDTO.getHierarchyLevel());
                isModified = true;
            }

            if (isModified) {
                String userUUID = departmentEntityRequest.getRequestHeader().getUserInfo() != null
                                    ? departmentEntityRequest.getRequestHeader().getUserInfo().getUuid()
                                    : null;

                existingDepartmentEntity.setLastModifiedBy(userUUID);

                existingDepartmentEntity.setLastModifiedTime(new Date().getTime());

                copyUpdatedRecordInDepartmentEntiyRequest(existingDepartmentEntity,
                        departmentEntityRequest.getDepartmentEntityDTO());

                kafkaProducer.push(ifixDepartmentEntityConfig.getPersisterKafkaDepartmentEntityUpdateTopic(),
                        departmentEntityRequest);
            }
        } else {
            throw new CustomException("INVALID_DEPARTMENT_ENTITY_ID", "Unable to find department entity by given id");

        }
        return departmentEntityRequest;
    }


    private void copyUpdatedRecordInDepartmentEntiyRequest(@NonNull DepartmentEntity existingDepartmentEntity,
                                                           @NonNull DepartmentEntityDTO departmentEntityDTO) {
        departmentEntityDTO.setId(existingDepartmentEntity.getId());
        departmentEntityDTO.setTenantId(existingDepartmentEntity.getTenantId());
        departmentEntityDTO.setDepartmentId(existingDepartmentEntity.getDepartmentId());
        departmentEntityDTO.setCode(existingDepartmentEntity.getCode());
        departmentEntityDTO.setName(existingDepartmentEntity.getName());
        departmentEntityDTO.setHierarchyLevel(existingDepartmentEntity.getHierarchyLevel());
        departmentEntityDTO.setAuditDetails(
                AuditDetails.builder()
                        .createdBy(existingDepartmentEntity.getCreatedBy())
                        .lastModifiedBy(existingDepartmentEntity.getLastModifiedBy())
                        .createdTime(existingDepartmentEntity.getCreatedTime())
                        .lastModifiedTime(existingDepartmentEntity.getLastModifiedTime())
                        .build()
        );
    }
}
