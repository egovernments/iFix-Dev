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
import java.util.*;
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
    private List<String> resolveChildIdList(String parentId, List<DepartmentEntityRelationship> entityRelationshipList) {
        List<String> childList = new ArrayList<>();

        if (!StringUtils.isEmpty(parentId) && entityRelationshipList != null && !entityRelationshipList.isEmpty()) {
            childList = entityRelationshipList.stream()
                    .filter(departmentEntityRelationship -> parentId.equals(departmentEntityRelationship.getParentId()))
                    .map(departmentEntityRelationship -> departmentEntityRelationship.getChildId())
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

    private List<String> getChildIdListFromDepartmentEntityRelationship(String departmentId) {
        List<String> childList = new ArrayList<>();

        if (!StringUtils.isEmpty(departmentId)) {
            List<DepartmentEntityRelationship> existingEntityRelationshipList =
                    entityRelationshipRepository.findByParentId(departmentId);

            if (existingEntityRelationshipList != null && !existingEntityRelationshipList.isEmpty()) {
                childList = existingEntityRelationshipList.stream()
                        .map(departmentEntityRelationship -> departmentEntityRelationship.getChildId())
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

    /**
     * @param departmentEntityRequest
     * @return
     */
    public @NonNull
    PersisterDepartmentEntityRequest wrapPersisterDepartmentEntityRequest(
            @NonNull DepartmentEntityRequest departmentEntityRequest) {

        PersisterDepartmentEntityRequest persisterDepartmentEntityRequest = PersisterDepartmentEntityRequest.builder()
                .requestHeader(departmentEntityRequest.getRequestHeader())
                .build();

        if (departmentEntityRequest.getDepartmentEntityDTO() != null
                && departmentEntityRequest.getDepartmentEntityDTO().getAuditDetails() != null) {

            List<PersisterDepartmentEntityRelationshipDTO> departmentEntityRelationshipDTOList =
                    wrapPersisterDepartmentEntityRelationshipDTOList(departmentEntityRequest.getDepartmentEntityDTO());

            PersisterDepartmentEntityDTO departmentEntityDTO = PersisterDepartmentEntityDTO.builder()
                    .id(departmentEntityRequest.getDepartmentEntityDTO().getId())
                    .tenantId(departmentEntityRequest.getDepartmentEntityDTO().getTenantId())
                    .departmentId(departmentEntityRequest.getDepartmentEntityDTO().getDepartmentId())
                    .code(departmentEntityRequest.getDepartmentEntityDTO().getCode())
                    .name(departmentEntityRequest.getDepartmentEntityDTO().getName())
                    .hierarchyLevel(departmentEntityRequest.getDepartmentEntityDTO().getHierarchyLevel())
                    .createdBy(departmentEntityRequest.getDepartmentEntityDTO().getAuditDetails().getCreatedBy())
                    .lastModifiedBy(departmentEntityRequest.getDepartmentEntityDTO().getAuditDetails().getLastModifiedBy())
                    .createdTime(departmentEntityRequest.getDepartmentEntityDTO().getAuditDetails().getCreatedTime())
                    .lastModifiedTime(departmentEntityRequest.getDepartmentEntityDTO().getAuditDetails().getLastModifiedTime())
                    .persisterDepartmentEntityRelationshipDTOS(departmentEntityRelationshipDTOList)
                    .build();

            persisterDepartmentEntityRequest
                    .setPersisterDepartmentEntityDtoList(Collections.singletonList(departmentEntityDTO));

        }

        return persisterDepartmentEntityRequest;
    }

    /**
     * @param departmentEntityDTO
     * @return
     */
    public List<PersisterDepartmentEntityRelationshipDTO> wrapPersisterDepartmentEntityRelationshipDTOList(
            @NonNull DepartmentEntityDTO departmentEntityDTO) {

        List<PersisterDepartmentEntityRelationshipDTO> departmentEntityRelationshipDTOList = new ArrayList<>();

        if (departmentEntityDTO.getChildren() != null && !departmentEntityDTO.getChildren().isEmpty()) {

            departmentEntityRelationshipDTOList = departmentEntityDTO.getChildren().stream()
                            .map(child ->
                                    PersisterDepartmentEntityRelationshipDTO.builder()
                                            .parentId(departmentEntityDTO.getId())
                                            .childId(child)
                                            .status(true)
                                            .build()
                            ).collect(Collectors.toList());
        }

        return departmentEntityRelationshipDTOList;
    }

    /**
     * @param departmentEntity
     * @return
     */
    public PersisterDepartmentEntityDTO wrapDepartmentEntityIntoPersisterDTO(@NonNull DepartmentEntity departmentEntity) {
        return PersisterDepartmentEntityDTO.builder()
                .id(departmentEntity.getId())
                .departmentId(departmentEntity.getDepartmentId())
                .name(departmentEntity.getName())
                .hierarchyLevel(departmentEntity.getHierarchyLevel())
                .code(departmentEntity.getCode())
                .tenantId(departmentEntity.getTenantId())
                .createdBy(departmentEntity.getCreatedBy())
                .createdTime(departmentEntity.getCreatedTime())
                .lastModifiedBy(departmentEntity.getLastModifiedBy())
                .lastModifiedTime(departmentEntity.getLastModifiedTime())
                .build();
    }

    public @NonNull
    PersisterDepartmentHierarchyLevelRequest wrapPersisterDepartmentHierarchyLevelRequest(
            @NonNull DepartmentHierarchyLevelRequest departmentHierarchyLevelRequest) {

        PersisterDepartmentHierarchyLevelRequest persisterRequest = PersisterDepartmentHierarchyLevelRequest.builder()
                .requestHeader(departmentHierarchyLevelRequest.getRequestHeader())
                .build();

        if (departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO() != null
                && departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getAuditDetails() != null) {

            PersisterDepartmentHierarchyLevelDTO hierarchyLevelDTO = PersisterDepartmentHierarchyLevelDTO.builder()
                    .id(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getId())
                    .tenantId(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getTenantId())
                    .departmentId(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getDepartmentId())
                    .label(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getLabel())
                    .parent(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getParent())
                    .level(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getLevel())
                    .createdBy(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getAuditDetails().getCreatedBy())
                    .lastModifiedBy(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getAuditDetails().getLastModifiedBy())
                    .createdTime(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getAuditDetails().getCreatedTime())
                    .lastModifiedTime(departmentHierarchyLevelRequest.getDepartmentHierarchyLevelDTO().getAuditDetails().getLastModifiedTime())
                    .build();

            persisterRequest.setDepartmentHierarchyLevelDTO(Collections.singletonList(hierarchyLevelDTO));
        }

        return persisterRequest;
    }
}
