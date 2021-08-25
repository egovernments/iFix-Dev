package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.DepartmentRepository;
import org.egov.validator.DepartmentValidator;
import org.egov.web.models.Department;
import org.egov.web.models.DepartmentSearchCriteria;
import org.egov.web.models.DepartmentSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DepartmentService {

    @Autowired
    private DepartmentValidator validator;

    @Autowired
    private DepartmentEnrichmentService enricher;

    @Autowired
    private DepartmentRepository departmentRepo;

    /**
     * Search the Department based on search criteria
     *
     * @param searchRequest
     * @return
     */
    public List<Department> departmentV1SearchPost(DepartmentSearchRequest searchRequest) {
        validator.validateSearchPost(searchRequest);
        enricher.enrichSearchPost(searchRequest);

        DepartmentSearchCriteria searchCriteria = searchRequest.getCriteria();
        if (searchCriteria.isEmpty())
            Collections.emptyList();

        List<Department> departments = departmentRepo.search(searchCriteria);

        if (departments == null || departments.isEmpty())
            Collections.emptyList();

        return departments;
    }
}
