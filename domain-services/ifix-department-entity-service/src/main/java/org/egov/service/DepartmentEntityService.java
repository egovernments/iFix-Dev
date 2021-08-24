package org.egov.service;

import org.egov.repository.DepartmentEntityRepository;
import org.egov.validator.DepartmentEntityValidator;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentEntityRequest;
import org.egov.web.models.DepartmentEntitySearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DepartmentEntityService {

    @Autowired
    DepartmentEntityValidator departmentEntityValidator;

    @Autowired
    DepartmentEntityEnrichmentService entityEnrichmentService;

    @Autowired
    DepartmentEntityRepository entityRepository;

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

    public List<DepartmentEntity> findAllByCriteria(DepartmentEntitySearchRequest departmentEntitySearchRequest) {
//        if(departmentEntitySearchRequest.getCriteria().getGetAncestry())
        return Collections.singletonList(entityRepository.getParent(departmentEntitySearchRequest.getCriteria().getIds().get(0)));
    }

}
