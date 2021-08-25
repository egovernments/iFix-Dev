package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.egov.web.models.DepartmentSearchCriteria;
import org.egov.web.models.DepartmentSearchRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepartmentEnrichmentService {

    /**
     *  Enrich the department search request
     * @param searchRequest
     */
    public void enrichSearchPost(DepartmentSearchRequest searchRequest) {
        DepartmentSearchCriteria searchCriteria = searchRequest.getCriteria();
        //TODO- fill if any default search criteria

    }
}
