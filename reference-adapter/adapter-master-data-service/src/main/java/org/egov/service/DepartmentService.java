package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.repository.DepartmentRepository;
import org.egov.util.DtoWrapper;
import org.egov.util.KafkaProducer;
import org.egov.validator.DepartmentValidator;
import org.egov.web.models.DepartmentDTO;
import org.egov.web.models.DepartmentRequest;
import org.egov.web.models.DepartmentSearchCriteria;
import org.egov.web.models.DepartmentSearchRequest;
import org.egov.web.models.persist.Department;
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

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private MasterDataServiceConfiguration masterDataServiceConfiguration;

    @Autowired
    private DtoWrapper dtoWrapper;

    /**
     * Search the DepartmentConst based on search criteria
     *
     * @param searchRequest
     * @return
     */
    public List<DepartmentDTO> departmentV1SearchPost(DepartmentSearchRequest searchRequest) {
        validator.validateSearchPost(searchRequest);
        enricher.enrichSearchPost(searchRequest);

        DepartmentSearchCriteria searchCriteria = searchRequest.getCriteria();
        if (searchCriteria.isEmpty())
            Collections.emptyList();

        List<Department> departments = departmentRepo.search(searchCriteria);

        if (departments == null || departments.isEmpty())
            Collections.emptyList();

        return dtoWrapper.wrapDepartmentEntityListIntoDTOs(departments);
    }

    /**
     * @param departmentRequest
     * @return
     */
    public DepartmentRequest createDepartment(DepartmentRequest departmentRequest) {
        validator.validateCreateRequestData(departmentRequest);
        enricher.enrichDepartmentData(departmentRequest);

        kafkaProducer.push(masterDataServiceConfiguration.getPersisterKafkaDepartmentCreateTopic(), departmentRequest);

        return departmentRequest;
    }
}
