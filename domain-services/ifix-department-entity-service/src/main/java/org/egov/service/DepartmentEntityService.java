package org.egov.service;

import org.egov.config.IfixDepartmentEntityConfig;
import org.egov.repository.DepartmentEntityRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityAncestryUtil;
import org.egov.validator.DepartmentEntityValidator;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<DepartmentEntity> departmentEntityList = entityRepository.searchEntity(departmentEntitySearchRequest);
        if(departmentEntitySearchRequest.getCriteria().isGetAncestry()) {
            List<DepartmentEntityAncestry> departmentEntityAncestryList = new ArrayList<>();
            for(DepartmentEntity departmentEntity : departmentEntityList) {
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
            if(parentEntity == null)
                break;

            DepartmentEntityAncestry parentAncestry =
                    departmentEntityAncestryUtil.createDepartmentEntityAncestryFromDepartmentEntity(parentEntity);
            parentAncestry.getChildren().add(ancestry);

            ancestry = parentAncestry;
            hierarchyCount--;
        }

        if(hierarchyCount < 0)
            throw new CustomException("MAXIMUM_SUPPORTED_HIERARCHY_EXCEEDED", "Loop to find ancestors " +
                    "exceeded the maximum supported hierarchy. The data might be corrupted.");

        return ancestry;
    }

}
