package org.egov.util;

import org.egov.common.contract.AuditDetails;
import org.egov.repository.DepartmentEntityRelationshipRepository;
import org.egov.web.models.*;
import org.egov.web.models.persist.DepartmentEntity;
import org.egov.web.models.persist.DepartmentEntityRelationship;
import org.egov.web.models.persist.DepartmentHierarchyLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoWrapper {

    @Autowired
    private DepartmentEntityRelationshipRepository entityRelationshipRepository;

    /**
     * @param departmentEntityList
     * @return
     */
    public List<DepartmentEntityDTO> wrapDepartmentEntityListIntoDTOs(List<DepartmentEntity> departmentEntityList) {
        List<DepartmentEntityDTO> departmentEntityDTOList = new ArrayList<>();

        if (departmentEntityList != null && !departmentEntityList.isEmpty()) {
            List<DepartmentEntityRelationship> deRelationshipList = entityRelationshipRepository
                    .findByParentIdList(getIdOfDepartmentEntity(departmentEntityList));

            departmentEntityDTOList = departmentEntityList.stream()
                    .map(departmentEntity -> wrapDepartmentEntityIntoDTO(departmentEntity, deRelationshipList))
                    .collect(Collectors.toList());
        }

        return departmentEntityDTOList;
    }

    public DepartmentEntityDTO wrapDepartmentEntityIntoDTO(@NonNull DepartmentEntity departmentEntity,
                                          @NonNull List<DepartmentEntityRelationship> deRelationshipList) {

        return DepartmentEntityDTO.builder()
                .id(departmentEntity.getId())
                .departmentId(departmentEntity.getDepartmentId())
                .name(departmentEntity.getName())
                .hierarchyLevel(departmentEntity.getHierarchyLevel())
                .code(departmentEntity.getCode())
                .tenantId(departmentEntity.getTenantId())
                .children(resolveChildIdList(departmentEntity.getId(), deRelationshipList))
                .auditDetails(
                        AuditDetails.builder()
                                .lastModifiedBy(departmentEntity.getLastModifiedBy())
                                .lastModifiedTime(
                                        departmentEntity.getLastModifiedTime() != null
                                                ? departmentEntity.getLastModifiedTime()
                                                : null
                                )
                                .createdBy(departmentEntity.getCreatedBy())
                                .createdTime(
                                        departmentEntity.getCreatedTime() != null
                                                ? departmentEntity.getCreatedTime()
                                                : null

                                )
                                .build()
                ).build();
    }

    /**
     * @param parentId
     * @param entityRelationshipList
     * @return
     */
    private List<DepartmentEntityChildrenDTO> resolveChildIdList(String parentId, List<DepartmentEntityRelationship> entityRelationshipList) {
        List<DepartmentEntityChildrenDTO> childList = new ArrayList<>();

        if (!StringUtils.isEmpty(parentId) && entityRelationshipList != null && !entityRelationshipList.isEmpty()) {
            childList = entityRelationshipList.stream()
                    .filter(departmentEntityRelationship -> parentId.equals(departmentEntityRelationship.getParentId()))
                    .map(departmentEntityRelationship ->
                            DepartmentEntityChildrenDTO.builder()
                                    .childId(departmentEntityRelationship.getChildId())
                                    .status(departmentEntityRelationship.getStatus())
                                    .build()
                    )
                    .collect(Collectors.toList());
        }

        return childList;
    }

    /**
     * @param departmentEntityList
     * @return
     */
    private List<String> getIdOfDepartmentEntity(List<DepartmentEntity> departmentEntityList) {
        List<String> idList = new ArrayList<>();

        if (departmentEntityList != null && !departmentEntityList.isEmpty()) {
            idList = departmentEntityList.stream()
                    .map(departmentEntity -> departmentEntity.getId())
                    .collect(Collectors.toList());

        }

        return idList;
    }


    /**
     * @param departmentEntity
     * @return
     */
    public DepartmentEntityDTO wrapDepartmentEntityIntoDTO(@NonNull DepartmentEntity departmentEntity) {
        return DepartmentEntityDTO.builder()
                .id(departmentEntity.getId())
                .departmentId(departmentEntity.getDepartmentId())
                .name(departmentEntity.getName())
                .hierarchyLevel(departmentEntity.getHierarchyLevel())
                .code(departmentEntity.getCode())
                .tenantId(departmentEntity.getTenantId())
                .children(getChildIdListFromDepartmentEntityRelationship(departmentEntity.getId()))
                .auditDetails(
                        AuditDetails.builder()
                                .lastModifiedBy(departmentEntity.getLastModifiedBy())
                                .lastModifiedTime(
                                        departmentEntity.getLastModifiedTime() != null
                                                ? departmentEntity.getLastModifiedTime()
                                                : null
                                )
                                .createdBy(departmentEntity.getCreatedBy())
                                .createdTime(
                                        departmentEntity.getCreatedTime() != null
                                                ? departmentEntity.getCreatedTime()
                                                : null

                                )
                                .build()
                ).build();
    }

    private List<DepartmentEntityChildrenDTO> getChildIdListFromDepartmentEntityRelationship(String departmentId) {
        List<DepartmentEntityChildrenDTO> childList = new ArrayList<>();

        if (!StringUtils.isEmpty(departmentId)) {
            List<DepartmentEntityRelationship> existingEntityRelationshipList =
                    entityRelationshipRepository.findByParentId(departmentId);

            if (existingEntityRelationshipList != null && !existingEntityRelationshipList.isEmpty()) {
                childList = existingEntityRelationshipList.stream()
                        .map(departmentEntityRelationship ->
                                DepartmentEntityChildrenDTO.builder()
                                        .childId(departmentEntityRelationship.getChildId())
                                        .status(departmentEntityRelationship.getStatus())
                                        .build()
                        )
                        .collect(Collectors.toList());
            }
        }

        return childList;
    }

    /**
     * @param departmentHierarchyLevelList
     * @return
     */
    public List<DepartmentHierarchyLevelDTO> wrapDepartmentHierarchyLevelIntoDTO(
            @NotNull List<DepartmentHierarchyLevel> departmentHierarchyLevelList) {

        return departmentHierarchyLevelList.stream()
                .map(departmentHierarchyLevel -> DepartmentHierarchyLevelDTO.builder()
                        .departmentId(departmentHierarchyLevel.getDepartmentId())
                        .id(departmentHierarchyLevel.getId())
                        .level(departmentHierarchyLevel.getLevel())
                        .label(departmentHierarchyLevel.getLabel())
                        .parent(departmentHierarchyLevel.getParent())
                        .tenantId(departmentHierarchyLevel.getTenantId())
                        .auditDetails(
                                AuditDetails.builder()
                                        .createdBy(departmentHierarchyLevel.getCreatedBy())
                                        .lastModifiedBy(departmentHierarchyLevel.getLastModifiedBy())
                                        .createdTime(departmentHierarchyLevel.getCreatedTime())
                                        .lastModifiedTime(departmentHierarchyLevel.getLastModifiedTime())
                                        .build()
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }

    public @NonNull
    List<String> getChildListFromDepatmentEntityChildren(@NonNull List<DepartmentEntityChildrenDTO>
                                                                 departmentEntityChildrenDTOList) {
        List<String> childList = new ArrayList<>();

        childList = departmentEntityChildrenDTOList.stream()
                .map(departmentEntityChildrenDTO -> departmentEntityChildrenDTO.getChildId())
                .collect(Collectors.toList());

        return childList;
    }
}
