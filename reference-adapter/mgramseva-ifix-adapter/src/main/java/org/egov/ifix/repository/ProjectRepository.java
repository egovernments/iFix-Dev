package org.egov.ifix.repository;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestHeader;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.models.Project;
import org.egov.ifix.models.ProjectRequest;
import org.egov.ifix.models.ProjectResponse;
import org.egov.ifix.models.ProjectSearchCriteria;
import org.egov.ifix.persistance.ProjectMapRepository;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.DataWrapper;
import org.egov.ifix.utils.RequestHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.egov.ifix.utils.EventConstants.PROJECT_CODE;

/**
 * @author mani This will be responsible for providing right Project from web or
 * db or file repository . Inorder to reduce manual mapping and in
 * future projects should be created via adapter only the following
 * steps used to fetch project code 1. Assuming client (mgram as of now)
 * and IFIX uses same code for Projects. 2. Adapter will search in IFIX
 * for the project passed by client by code . If single project found
 * returns it. 3. If Multiple projects found then adapter will look for
 * mapping table a. In mapping table unique entry for given code found
 * corresponding ifixid is returned b. if multiple entry found throws
 * runtime exception c. if not data found throws runtime exception
 */
@Repository
@Slf4j
public class ProjectRepository {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private RequestHeaderUtil requestHeaderUtil;

    @Autowired
    private ProjectMapRepository projectMapRepository;

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private DataWrapper dataWrapper;

    /**
     * @param projectCode
     * @param jsonObjectData
     * @return
     */
    public Optional<Project> getProjectFromIFixMasterService(String projectCode, JsonObject jsonObjectData) {
        if (!StringUtils.isEmpty(projectCode) && jsonObjectData != null) {
            String url = applicationConfiguration.getIfixHost() + applicationConfiguration.getProjectSearchEndpoint();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authTokenService.getKeyCloakAuthToken());

            ProjectSearchCriteria criteria = new ProjectSearchCriteria();
            criteria.setCode(projectCode);
            criteria.setTenantId(applicationConfiguration.getTenantId());
            RequestHeader requestHeader = new RequestHeader();
            requestHeader = requestHeaderUtil.populateRequestHeader(jsonObjectData, requestHeader);

            ProjectRequest projectRequest = new ProjectRequest();
            projectRequest.setRequestHeader(requestHeader);
            projectRequest.setProjectSearchCriteria(criteria);

            try {
                HttpEntity<ProjectRequest> request = new HttpEntity<>(projectRequest, headers);

                ResponseEntity<ProjectResponse> response = restTemplate.postForEntity(url, request, ProjectResponse.class);
                List<Project> projectList = response.getBody().getProjects();

                if (projectList != null && projectList.size() == 1) {
                    return Optional.ofNullable(projectList.get(0));

                } else if (projectList == null || projectList.isEmpty()) {
                    wrapErrorModelAndPushKafkaTopic(projectCode, "No project details found in iFix Core");
                    throw new HttpCustomException(PROJECT_CODE, "Invalid project search request", HttpStatus.BAD_REQUEST);

                } else if (projectList.size() > 1) {
                    wrapErrorModelAndPushKafkaTopic(projectCode, "Duplicate project found in iFix Core");
                    throw new HttpCustomException(PROJECT_CODE, "Invalid project search request", HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                wrapErrorModelAndPushKafkaTopic(projectCode, "Exception while sending request to iFix " +
                        "for project search");
                throw new HttpCustomException(PROJECT_CODE, "Exception while sending request to iFix for project " +
                        "search", HttpStatus.BAD_REQUEST);
            }
        }
        return Optional.empty();
    }


    /**
     * @param attributeValue
     * @param errorMessage
     */
    private void wrapErrorModelAndPushKafkaTopic(String attributeValue, String errorMessage) {
//TODO: Error handling stream.
/*
        Optional<ErrorDataModel> errorDataModelOptional = dataWrapper.getErrorDataModel(NA,
                PROJECT_CODE_DATA_NAME, CODE, attributeValue, NON_RECOVERABLE_ERROR,
                HttpStatus.BAD_REQUEST.toString(), errorMessage);

        if (errorDataModelOptional.isPresent()) {
            kafkaTemplate.send(applicationConfiguration.getErrorTopicName(), errorDataModelOptional.get());
        }
*/
    }

}
