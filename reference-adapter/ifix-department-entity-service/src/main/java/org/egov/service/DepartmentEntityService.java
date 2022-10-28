package org.egov.service;

import org.egov.config.IfixDepartmentEntityConfig;
import org.egov.repository.DepartmentEntityRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityAncestryUtil;
import org.egov.validator.DepartmentEntityValidator;
import org.egov.web.models.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentEntityService {

    @Autowired
    DepartmentEntityValidator departmentEntityValidator;

    @Autowired
    DepartmentEntityEnrichmentService entityEnrichmentService;

    @Autowired
    DepartmentEntityRepository entityRepository;

    @Autowired
    private IfixDepartmentEntityConfig ifixDepartmentEntityConfig;

    @Autowired
    private DepartmentEntityAncestryUtil departmentEntityAncestryUtil;

    /**
     * @param departmentEntityRequest
     * @return
     */
    public DepartmentEntityRequest createDepartmentEntity(DepartmentEntityRequest departmentEntityRequest) {
        departmentEntityValidator.validateDepartmentEntityRequest(departmentEntityRequest);
        entityEnrichmentService.enrichDepartmentEntityData(departmentEntityRequest);
        entityRepository.save(departmentEntityRequest.getDepartmentEntity());

        return departmentEntityRequest;
    }

    public List<? extends DepartmentEntityAbstract> findAllByCriteria(DepartmentEntitySearchRequest departmentEntitySearchRequest) {
        departmentEntityValidator.validateSearchDepartmentEntity(departmentEntitySearchRequest);
        List<DepartmentEntity> departmentEntityList = entityRepository.searchEntity(departmentEntitySearchRequest);
        if (departmentEntitySearchRequest.getCriteria().isGetAncestry()) {
            List<DepartmentEntityAncestry> departmentEntityAncestryList = new ArrayList<>();
            for (DepartmentEntity departmentEntity : departmentEntityList) {
                departmentEntityAncestryList.add(createAncestryFor(departmentEntity));
            }
            return departmentEntityAncestryList;
        }
        return departmentEntityList;
    }


    public DepartmentEntityAncestry createAncestryFor(DepartmentEntity departmentEntity) {
        int hierarchyCount = ifixDepartmentEntityConfig.getMaximumSupportedDepartmentHierarchy();
        DepartmentEntityAncestry ancestry =
                departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity(departmentEntity);
        while (hierarchyCount >= 0) {
            DepartmentEntity parentEntity = entityRepository.getParent(ancestry.getId());
            if (parentEntity == null)
                break;

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

        DepartmentEntity requestedDepartmentEntity = departmentEntityRequest.getDepartmentEntity();

        Optional<DepartmentEntity> departmentEntityOptional = entityRepository
                .findById(requestedDepartmentEntity.getId());

        if (departmentEntityOptional.isPresent()) {
            DepartmentEntity existingDepartmentEntity = departmentEntityOptional.get();

            if (!StringUtils.isEmpty(requestedDepartmentEntity.getTenantId())) {
                existingDepartmentEntity.setTenantId(requestedDepartmentEntity.getTenantId());
                isModified = true;
            }

            if (!StringUtils.isEmpty(requestedDepartmentEntity.getDepartmentId())) {
                existingDepartmentEntity.setDepartmentId(requestedDepartmentEntity.getDepartmentId());
                isModified = true;
            }

            if (!StringUtils.isEmpty(requestedDepartmentEntity.getCode())) {
                existingDepartmentEntity.setCode(requestedDepartmentEntity.getCode());
                isModified = true;
            }

            if (!StringUtils.isEmpty(requestedDepartmentEntity.getName())) {
                existingDepartmentEntity.setName(requestedDepartmentEntity.getName());
                isModified = true;
            }

            if (requestedDepartmentEntity.getHierarchyLevel() != null) {
                existingDepartmentEntity.setHierarchyLevel(requestedDepartmentEntity.getHierarchyLevel());
                isModified = true;
            }

            if (requestedDepartmentEntity.getChildren() != null && !requestedDepartmentEntity.getChildren().isEmpty()) {
                existingDepartmentEntity.setChildren(requestedDepartmentEntity.getChildren());
                isModified = true;
            }

            if (isModified) {
                existingDepartmentEntity.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
                entityRepository.save(existingDepartmentEntity);

                BeanUtils.copyProperties(existingDepartmentEntity, departmentEntityRequest.getDepartmentEntity());
            }
        } else {
            throw new CustomException("INVALID_DEPARTMENT_ENTITY_ID", "Unable to find department entity by given id");

        }
        return departmentEntityRequest;
    }
}
