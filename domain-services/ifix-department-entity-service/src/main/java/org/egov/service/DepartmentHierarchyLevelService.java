package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.validator.DepartmentHierarchyLevelValidator;
import org.egov.web.models.DepartmentHierarchyLevelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepartmentHierarchyLevelService {

    @Autowired
    private DepartmentHierarchyLevelValidator validator;

    @Autowired
    private DepartmentHierarchyLevelEnrichmentService enricher;

    @Autowired
    private DepartmentHierarchyLevelRepository hierarchyLevelRepository;

    /**
     * validate the department hierarchy level request , enrich and save the details in
     * db and return the enriched request
     *
     * @param request
     * @return
     */
    public DepartmentHierarchyLevelRequest departmentEntityHierarchyLevelV1CreatePost(DepartmentHierarchyLevelRequest request) {
        validator.validateHierarchyLevelCreatePost(request);
        enricher.enrichHierarchyLevelCreatePost(request);
        hierarchyLevelRepository.save(request.getDepartmentHierarchyLevel());
        return request;
    }
}
