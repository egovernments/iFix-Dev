package org.egov.ifixmigrationtoolkit.util;

import org.egov.ifixmigrationtoolkit.models.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ObjectWrapper {
    /**
     * @param departmentEntityResponse
     * @return
     */
    public @NonNull
    PersisterDepartmentEntityRequest wrapPersisterDepartmentEntityRequest(
            @NonNull DepartmentEntityResponse departmentEntityResponse) {

        PersisterDepartmentEntityRequest persisterDepartmentEntityRequest = new PersisterDepartmentEntityRequest();

        if (departmentEntityResponse.getDepartmentEntity() != null && !departmentEntityResponse.getDepartmentEntity().isEmpty()) {
            List<DepartmentEntity> departmentEntityList = departmentEntityResponse.getDepartmentEntity();

            List<PersisterDepartmentEntity> persisterDepartmentEntityList = departmentEntityList.stream()
                    .map(departmentEntity ->
                            PersisterDepartmentEntity.builder()
                                    .id(departmentEntity.getId())
                                    .tenantId(departmentEntity.getTenantId())
                                    .departmentId(departmentEntity.getDepartmentId())
                                    .code(departmentEntity.getCode())
                                    .name(departmentEntity.getName())
                                    .hierarchyLevel(departmentEntity.getHierarchyLevel())
                                    .createdBy(departmentEntity.getAuditDetails().getCreatedBy())
                                    .lastModifiedBy(departmentEntity.getAuditDetails().getLastModifiedBy())
                                    .createdTime(departmentEntity.getAuditDetails().getCreatedTime())
                                    .lastModifiedTime(departmentEntity.getAuditDetails().getLastModifiedTime())
                                    .children(String.join(",", departmentEntity.getChildren()))
                                    .build()

                    ).collect(Collectors.toList());

            persisterDepartmentEntityRequest.setDepartmentEntity(persisterDepartmentEntityList);
        }

        return persisterDepartmentEntityRequest;
    }

    /**
     * @param departmentHierarchyLevelResponse
     * @return
     */
    public @NonNull
    PersisterDepartmentHierarchyLevelRequest wrapPersisterDepartmentHierarchyLevelRequest(
            @NonNull DepartmentHierarchyLevelResponse departmentHierarchyLevelResponse) {

        PersisterDepartmentHierarchyLevelRequest persisterDepartmentHierarchyLevelRequest =
                new PersisterDepartmentHierarchyLevelRequest();

        if (departmentHierarchyLevelResponse.getDepartmentHierarchyLevel() != null
                && !departmentHierarchyLevelResponse.getDepartmentHierarchyLevel().isEmpty()) {
            List<DepartmentHierarchyLevel> departmentHierarchyLevelList = departmentHierarchyLevelResponse
                    .getDepartmentHierarchyLevel();

            List<PersisterDepartmentHierarchyLevel> persisterDepartmentHierarchyLevelsList =
                    departmentHierarchyLevelList.stream()
                    .map(departmentHierarchyLevel ->
                            PersisterDepartmentHierarchyLevel.builder()
                                    .id(departmentHierarchyLevel.getId())
                                    .tenantId(departmentHierarchyLevel.getTenantId())
                                    .departmentId(departmentHierarchyLevel.getDepartmentId())
                                    .label(departmentHierarchyLevel.getLabel())
                                    .parent(departmentHierarchyLevel.getParent())
                                    .level(departmentHierarchyLevel.getLevel())
                                    .createdBy(departmentHierarchyLevel.getAuditDetails().getCreatedBy())
                                    .lastModifiedBy(departmentHierarchyLevel.getAuditDetails().getLastModifiedBy())
                                    .createdTime(departmentHierarchyLevel.getAuditDetails().getCreatedTime())
                                    .lastModifiedTime(departmentHierarchyLevel.getAuditDetails().getLastModifiedTime())
                                    .build()

                    ).collect(Collectors.toList());

            persisterDepartmentHierarchyLevelRequest.setDepartmentHierarchyLevel(persisterDepartmentHierarchyLevelsList);
        }

        return persisterDepartmentHierarchyLevelRequest;
    }
}
