package org.egov.util;

import org.egov.common.contract.AuditDetails;
import org.egov.web.models.*;
import org.egov.web.models.persist.DepartmentEntity;
import org.egov.web.models.persist.DepartmentHierarchyLevel;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DtoWrapper {

    /**
     * @param departmentEntityList
     * @return
     */
    public List<DepartmentEntityDTO> wrapDepartmentEntityListIntoDTOs(List<DepartmentEntity> departmentEntityList) {
        List<DepartmentEntityDTO> departmentEntityDTOList = new ArrayList<>();

        if (departmentEntityList != null && !departmentEntityList.isEmpty()) {
            departmentEntityDTOList = departmentEntityList.stream()
                    .map(this::wrapDepartmentEntityIntoDTO)
                    .collect(Collectors.toList());
        }

        return departmentEntityDTOList;
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
//                    .children(String.join(",", departmentEntityRequest.getDepartmentEntityDTO().getChildren()))
                    .build();

            persisterDepartmentEntityRequest
                    .setPersisterDepartmentEntityDtoList(Collections.singletonList(departmentEntityDTO));

            List<PersisterDepartmentEntityRelationshipDTO> departmentEntityRelationshipDTOList =
                    wrapPersisterDepartmentEntityRelationshipDTOList(departmentEntityRequest.getDepartmentEntityDTO());

            if (departmentEntityRelationshipDTOList != null && !departmentEntityRelationshipDTOList.isEmpty()) {
                persisterDepartmentEntityRequest
                        .setPersisterDepartmentEntityRelationshipDTOS(departmentEntityRelationshipDTOList);
            }
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
                                            .isTrue(true)
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
