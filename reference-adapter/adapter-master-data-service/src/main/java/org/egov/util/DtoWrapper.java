package org.egov.util;

import org.egov.common.contract.AuditDetails;
import org.egov.repository.ProjectDepartmentEntityRelationRepository;
import org.egov.web.models.DepartmentDTO;
import org.egov.web.models.ExpenditureDTO;
import org.egov.web.models.ProjectDTO;
import org.egov.web.models.ProjectDepartmentEntityRelationshipDTO;
import org.egov.web.models.persist.Department;
import org.egov.web.models.persist.Expenditure;
import org.egov.web.models.persist.Project;
import org.egov.web.models.persist.ProjectDepartmentEntityRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoWrapper {

    @Autowired
    private ProjectDepartmentEntityRelationRepository projectDepartmentEntityRelationRepository;

    public List<ProjectDTO> getProjectDTOListWithRelationship(List<Project> projectList) {
        if (projectList != null && !projectList.isEmpty()) {
            List<String> projectIdList = projectList.stream().map(project -> project.getId()).collect(Collectors.toList());

            List<ProjectDepartmentEntityRelationship> departmentEntityRelationshipList =
                    projectDepartmentEntityRelationRepository.findByProjectIdList(projectIdList);

            return projectList.stream()
                    .map(project ->
                        ProjectDTO.builder()
                            .id(project.getId())
                            .tenantId(project.getTenantId())
                            .code(project.getCode())
                            .name(project.getName())
                            .expenditureId(project.getExpenditureId())
                            .departmentEntityIds(
                                getDepartmentEntityListForProject(project.getId(), departmentEntityRelationshipList)
                            )
                            .auditDetails(
                                AuditDetails.builder()
                                    .lastModifiedBy(project.getLastModifiedBy())
                                    .lastModifiedTime(project.getLastModifiedTime())
                                    .createdBy(project.getCreatedBy())
                                    .createdTime(project.getCreatedTime())
                                    .build()
                            )
                        .build()
                        )
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * @param projectId
     * @param departmentEntityRelationshipList
     * @return
     */
    private List<ProjectDepartmentEntityRelationshipDTO> getDepartmentEntityListForProject(String projectId,
                                           List<ProjectDepartmentEntityRelationship> departmentEntityRelationshipList) {

        if (departmentEntityRelationshipList != null && !departmentEntityRelationshipList.isEmpty()
                && !StringUtils.isEmpty(projectId)) {

            return departmentEntityRelationshipList.stream()
                    .filter(projectDepartmentEntityRelationship -> projectDepartmentEntityRelationship != null)
                    .filter(projectDepartmentEntityRelationship ->
                            projectDepartmentEntityRelationship.getProjectId().equals(projectId))
                    .map(projectDepartmentEntityRelationship ->
                        ProjectDepartmentEntityRelationshipDTO.builder()
                            .departmentEntityId(projectDepartmentEntityRelationship.getDepartmentEntityId())
                            .status(projectDepartmentEntityRelationship.getStatus())
                            .build()
                    )
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * @param project
     * @return
     */
    public ProjectDTO wrapProjectIntoProjectDTO(@NonNull Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .tenantId(project.getTenantId())
                .code(project.getCode())
                .name(project.getName())
                .expenditureId(project.getExpenditureId())
                .auditDetails(
                    AuditDetails.builder()
                        .lastModifiedBy(project.getLastModifiedBy())
                        .lastModifiedTime(project.getLastModifiedTime())
                        .createdBy(project.getCreatedBy())
                        .createdTime(project.getCreatedTime())
                        .build()
                )
                .build();
    }


    /**
     * @param expenditureList
     * @return
     */
    public @NonNull
    List<ExpenditureDTO> wrapExpenditureDTOByEntity(@NonNull List<Expenditure> expenditureList) {
        List<ExpenditureDTO> expenditureDTOList = new ArrayList<>();

        expenditureDTOList = expenditureList.stream()
                .map(expenditure -> ExpenditureDTO.builder()
                        .id(expenditure.getId())
                        .tenantId(expenditure.getTenantId())
                        .code(expenditure.getCode())
                        .name(expenditure.getName())
                        .type(ExpenditureDTO.TypeEnum.fromValue(expenditure.getType()))
                        .departmentId(expenditure.getDepartmentId())
                        .auditDetails(
                                AuditDetails.builder()
                                        .lastModifiedBy(expenditure.getLastModifiedBy())
                                        .lastModifiedTime(expenditure.getLastModifiedTime())
                                        .createdBy(expenditure.getCreatedBy())
                                        .createdTime(expenditure.getCreatedTime() )
                                        .build()
                        )
                        .build()
                ).collect(Collectors.toList());
        return expenditureDTOList;
    }


    /**
     * @param departmentList
     * @return
     */
    public List<DepartmentDTO> wrapDepartmentEntityListIntoDTOs(List<Department> departmentList) {
        List<DepartmentDTO> departmentEntityDTOList = new ArrayList<>();

        if (departmentList != null && !departmentList.isEmpty()) {
            departmentEntityDTOList = departmentList.stream()
                    .map(this::wrapDepartmentDtoRequest)
                    .collect(Collectors.toList());
        }

        return departmentEntityDTOList;
    }

    /**
     * @param department
     * @return
     */
    public @NonNull
    DepartmentDTO wrapDepartmentDtoRequest(@NonNull Department department) {

        return DepartmentDTO.builder()
                .id(department.getId())
                .tenantId(department.getTenantId())
                .code(department.getCode())
                .name(department.getName())
                .isNodal(department.getIsNodal())
                .parent(department.getParent())
                .auditDetails(
                        AuditDetails.builder()
                                .lastModifiedBy(department.getLastModifiedBy())
                                .lastModifiedTime(department.getLastModifiedTime())
                                .createdBy(department.getCreatedBy())
                                .createdTime(department.getCreatedTime())
                                .build()
                )
                .build();
    }

    /**
     * @param departmentEntityRelationshipDtoList
     * @return
     */
    public @NonNull
    List<String> getIdListFromDepartmentEntityDTO(@NonNull List<ProjectDepartmentEntityRelationshipDTO>
                                                                 departmentEntityRelationshipDtoList) {
        List<String> departmentEntityIdList = new ArrayList<>();

        departmentEntityIdList = departmentEntityRelationshipDtoList.stream()
                .map(departmentEntityDTO -> departmentEntityDTO.getDepartmentEntityId())
                .collect(Collectors.toList());

        return departmentEntityIdList;
    }
}
