package org.egov.ifix.repository;

import java.util.Arrays;
import java.util.List;

import org.egov.common.contract.request.RequestHeader;
import org.egov.ifix.models.Project;
import org.egov.ifix.models.ProjectRequest;
import org.egov.ifix.models.ProjectResponse;
import org.egov.ifix.models.ProjectSearchCriteria;
import org.egov.ifix.persistance.ProjectMap;
import org.egov.ifix.persistance.ProjectMapRepository;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.RequestHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author mani This will be responsible for providing right Project from web or
 *         db or file repository . Inorder to reduce manual mapping and in
 *         future projects should be created via adapter only the following
 *         steps used to fetch project code 1. Assuming client (mgram as of now)
 *         and IFIX uses same code for Projects. 2. Adapter will search in IFIX
 *         for the project passed by client by code . If single project found
 *         returns it. 3. If Multiple projects found then adapter will look for
 *         mapping table a. In mapping table unique entry for given code found
 *         corresponding ifixid is returned b. if multiple entry found throws
 *         runtime exception c. if not data found throws runtime exception
 * 
 * 
 *
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

	public Project getProject(String code, JsonObject data) {

		log.debug("getting Project from ifix .........");

		String url = applicationConfiguration.getIfixHost() + applicationConfiguration.getProjectSearchApi();

		log.info("Searching in ifix  for Project ");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authTokenService.getAuthToken());

		ProjectSearchCriteria criteria = new ProjectSearchCriteria();
		criteria.setCode(code);
		criteria.setTenantId(applicationConfiguration.getTenantId());
		RequestHeader requestHeader = new RequestHeader();
		requestHeader = requestHeaderUtil.polulateRequestHeader(data, requestHeader);

		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setRequestHeader(requestHeader);
		projectRequest.setProjectSearchCriteria(criteria);

		HttpEntity<ProjectRequest> request = new HttpEntity<>(projectRequest, headers);

		ResponseEntity<ProjectResponse> response = restTemplate.postForEntity(url, request, ProjectResponse.class);

		log.info("Searching in IFix status" + response.getStatusCode());
		List<Project> projectList = response.getBody().getProjects();
		if (projectList.size() > 1 || projectList.isEmpty()) {
			ProjectMap project = null;
			List<ProjectMap> projects = projectMapRepository.findByClientProjectCode(code);
			if (projects.size() > 1)
				throw new RuntimeException("Duplicate Project Mapping in table");
			else if (projects.size() == 1) {
				project = projects.get(0);
				criteria.setCode(null);
				List<String> ids = Arrays.asList(new String[] { project.getIFixProjectId() });
				criteria.setIds(ids);
				projectRequest.setProjectSearchCriteria(criteria);
				request = new HttpEntity<>(projectRequest, headers);
				response = restTemplate.postForEntity(url, request, ProjectResponse.class);
				projectList = response.getBody().getProjects();
				log.info("got project by matching using  mapping table");
				return  projectList.get(0);
			} else {
				throw new RuntimeException(
						"Duplicate projects found for the code in ifix and No Project Mapping  found in table ."
								+ " Use the mapping table to uniquely identify project");
			}

		} else if (projectList.size() == 1) {
			log.info("got project by matching code");
			return projectList.get(0);

		}

		return null;
	}

}
