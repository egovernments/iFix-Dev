package org.egov.ifixmigrationtoolkit.util;

import org.egov.common.contract.AuditDetails;
import org.egov.ifixmigrationtoolkit.models.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ObjectWrapper {

    /**
     * @param departmentEntityResponse
     * @return
     */
    public @NonNull
    List<PersisterDepartmentEntityRequest> wrapPersisterDepartmentEntityRequest(
            @NonNull DepartmentEntityResponse departmentEntityResponse) {

        List<PersisterDepartmentEntityRequest> persisterDepartmentEntityRequest = new ArrayList<>();

        if (departmentEntityResponse.getDepartmentEntity() != null && !departmentEntityResponse.getDepartmentEntity().isEmpty()) {
            List<DepartmentEntity> departmentEntityList = departmentEntityResponse.getDepartmentEntity();

            persisterDepartmentEntityRequest = departmentEntityList.stream()
                    .map(departmentEntity ->
                        PersisterDepartmentEntityRequest.builder()
                            .departmentEntity(
                                PersisterDepartmentEntity.builder()
                                    .id(departmentEntity.getId())
                                    .tenantId(departmentEntity.getTenantId())
                                    .departmentId(departmentEntity.getDepartmentId())
                                    .code(departmentEntity.getCode())
                                    .name(departmentEntity.getName())
                                    .hierarchyLevel(departmentEntity.getHierarchyLevel())
                                    .auditDetails(
                                            AuditDetails.builder()
                                                    .lastModifiedBy(departmentEntity.getAuditDetails().getLastModifiedBy())
                                                    .lastModifiedTime(departmentEntity.getAuditDetails().getLastModifiedTime())
                                                    .createdBy(departmentEntity.getAuditDetails().getCreatedBy())
                                                    .createdTime(departmentEntity.getAuditDetails().getCreatedTime())
                                                    .build()
                                    )
                                    .children(getDepartmentEntityChildren(departmentEntity.getChildren()))
                                    .build()
                                )
                                .build()

                    ).collect(Collectors.toList());

        }

        return persisterDepartmentEntityRequest;
    }

    /**
     * @param childList
     * @return
     */
    private List<PersisterDepartmentEntityChildren> getDepartmentEntityChildren(List<String> childList) {
        if (childList != null && !childList.isEmpty()) {
            return childList.stream()
                    .filter(child -> child != null && !child.isEmpty())
                    .map(child -> PersisterDepartmentEntityChildren.builder()
                            .childId(child)
                            .status(true)
                            .build()
                    ).collect(Collectors.toList());
        }

        return null;
    }


    /**
     * @param departmentHierarchyLevelResponse
     * @return
     */
    public @NonNull
    List<PersisterDepartmentHierarchyLevelRequest> wrapPersisterDepartmentHierarchyLevelRequest(
            @NonNull DepartmentHierarchyLevelResponse departmentHierarchyLevelResponse) {

        List<PersisterDepartmentHierarchyLevelRequest> persisterDepartmentHierarchyLevelsList = new ArrayList<>();

        if (departmentHierarchyLevelResponse.getDepartmentHierarchyLevel() != null
                && !departmentHierarchyLevelResponse.getDepartmentHierarchyLevel().isEmpty()) {
            List<DepartmentHierarchyLevel> departmentHierarchyLevelList = departmentHierarchyLevelResponse
                    .getDepartmentHierarchyLevel();

            persisterDepartmentHierarchyLevelsList = departmentHierarchyLevelList.stream()
                    .map(departmentHierarchyLevel ->
                        PersisterDepartmentHierarchyLevelRequest.builder()
                            .departmentHierarchyLevel(
                                PersisterDepartmentHierarchyLevel.builder()
                                    .id(departmentHierarchyLevel.getId())
                                    .tenantId(departmentHierarchyLevel.getTenantId())
                                    .departmentId(departmentHierarchyLevel.getDepartmentId())
                                    .label(departmentHierarchyLevel.getLabel())
                                    .parent(departmentHierarchyLevel.getParent())
                                    .level(departmentHierarchyLevel.getLevel())
                                    .auditDetails(
                                        AuditDetails.builder()
                                            .lastModifiedBy(departmentHierarchyLevel.getAuditDetails().getLastModifiedBy())
                                            .lastModifiedTime(departmentHierarchyLevel.getAuditDetails().getLastModifiedTime())
                                            .createdBy(departmentHierarchyLevel.getAuditDetails().getCreatedBy())
                                            .createdTime(departmentHierarchyLevel.getAuditDetails().getCreatedTime())
                                            .build()
                                    )
                                    .build()
                            ).build()
                    ).collect(Collectors.toList());
        }
        return persisterDepartmentHierarchyLevelsList;
    }
}
