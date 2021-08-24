package org.egov.service;

import org.egov.repository.DepartmentEntityRepository;
import org.egov.validator.DepartmentEntityValidator;
import org.egov.web.models.DepartmentEntityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
