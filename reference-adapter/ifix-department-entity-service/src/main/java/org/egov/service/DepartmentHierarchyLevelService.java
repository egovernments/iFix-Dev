package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.config.IfixDepartmentEntityConfig;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.util.DtoWrapper;
import org.egov.util.KafkaProducer;
import org.egov.validator.DepartmentHierarchyLevelValidator;
import org.egov.web.models.DepartmentHierarchyLevelDTO;
import org.egov.web.models.DepartmentHierarchyLevelRequest;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.egov.web.models.DepartmentHierarchyLevelSearchRequest;
import org.egov.web.models.persist.DepartmentHierarchyLevel;
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

    @Autowired
    private IfixDepartmentEntityConfig ifixDepartmentEntityConfig;

    @Autowired
    private DtoWrapper dtoWrapper;

    @Autowired
    private KafkaProducer kafkaProducer;

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

        kafkaProducer.push(ifixDepartmentEntityConfig.getPersisterKafkaDepartmentHierarchyCreateTopic(),
                dtoWrapper.wrapPersisterDepartmentHierarchyLevelRequest(request));

        return request;
    }

    /**
     * Validate the search request, enrich, search w.r.t parameters And return the result.
     *
     * @param searchRequest
     * @return
     */
    public List<DepartmentHierarchyLevelDTO> searchDepartmentEntityHierarchyLevel(DepartmentHierarchyLevelSearchRequest searchRequest) {
        validator.validateHierarchyLevelSearchPost(searchRequest);
        DepartmentHierarchyLevelSearchCriteria searchCriteria = searchRequest.getCriteria();

        if ((searchCriteria.getIds() == null || searchCriteria.getIds().isEmpty()) && StringUtils.isBlank(searchCriteria.getDepartmentId())
                && StringUtils.isBlank(searchCriteria.getLabel()) && StringUtils.isBlank(searchCriteria.getTenantId())
                && searchCriteria.getLevel() == null) {

            return Collections.emptyList();
        }

        List<DepartmentHierarchyLevel> departmentHierarchyLevelList = hierarchyLevelRepository.findByParamExistence(searchCriteria);

        if (departmentHierarchyLevelList == null || departmentHierarchyLevelList.isEmpty())
            return Collections.emptyList();

        return dtoWrapper.wrapDepartmentHierarchyLevelIntoDTO(departmentHierarchyLevelList);
    }
}
