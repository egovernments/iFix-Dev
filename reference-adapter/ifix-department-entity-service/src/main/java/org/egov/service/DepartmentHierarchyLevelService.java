package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.validator.DepartmentHierarchyLevelValidator;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
    public DepartmentHierarchyLevelRequest createDepartmentEntityHierarchyLevel(DepartmentHierarchyLevelRequest request) {
        validator.validateHierarchyLevelCreatePost(request);
        enricher.enrichHierarchyLevelCreatePost(request);
        hierarchyLevelRepository.save(request.getDepartmentHierarchyLevel());
        return request;
    }

    /**
     * Validate the search request, enrich, search w.r.t parameters And return the result.
     *
     * @param searchRequest
     * @return
     */
    public List<DepartmentHierarchyLevel> searchDepartmentEntityHierarchyLevel(DepartmentHierarchyLevelSearchRequest searchRequest) {
        validator.validateHierarchyLevelSearchPost(searchRequest);
        DepartmentHierarchyLevelSearchCriteria searchCriteria = searchRequest.getCriteria();

        if ((searchCriteria.getIds() == null || searchCriteria.getIds().isEmpty()) && StringUtils.isBlank(searchCriteria.getDepartmentId())
                && StringUtils.isBlank(searchCriteria.getLabel()) && StringUtils.isBlank(searchCriteria.getTenantId())
                && searchCriteria.getLevel() == null) {

            return Collections.emptyList();
        }

        List<DepartmentHierarchyLevel> departmentHierarchyLevels = hierarchyLevelRepository.searchDeptHierarchyLevel(searchCriteria);

        if (departmentHierarchyLevels == null || departmentHierarchyLevels.isEmpty())
            return Collections.emptyList();

        return departmentHierarchyLevels;
    }

    /**
     * @param body
     * @return
     */
    public Long getDepartmentHierarchyLevelCount(DepartmentEntityPlainSearchRequest body) {
        return hierarchyLevelRepository.getDepartmentHierarchyLevelCount(body.getCriteria());
    }

    /**
     * @param plainSearchRequest
     * @return
     */
    public List<DepartmentHierarchyLevel> departmentHierarchyPlainSearchPost(
            DepartmentEntityPlainSearchRequest plainSearchRequest) {

        List<DepartmentHierarchyLevel> plainDepartmentHierarchyList = hierarchyLevelRepository
                .plainSearchDepartmentEntity(plainSearchRequest.getCriteria());

        if (plainDepartmentHierarchyList == null || plainDepartmentHierarchyList.isEmpty())
            return Collections.emptyList();
        else
            return plainDepartmentHierarchyList;
    }
}
