package org.egov.ifix.service;

import org.egov.ifix.cache.AdapterCache;
import org.egov.ifix.models.Project;
import org.egov.ifix.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author mani 
 *         This will manage getting the right Project in running
 *         environment. This will cache the data once received from repository.
 *         Expiry is not set it will be global config which will clear cache on
 *         specified time. It depends on repository to provide project object for
 *         given code. 
 */
@Service
@Slf4j
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private AdapterCache<Project> projectCache;

	private static final Long TIMEBEFOREEXPIRY = 5l * 60 * 1000;

	public String getProjectId(String code,JsonObject data) {

		Project project = projectCache.getValue(code);
		if (project == null) {
			project = projectRepository.getProject(code,data);

			if (project != null) {
				 
				projectCache.putValue(code, project);
				 
			}
		} else {
			log.info("got project from Cache", project.getId());
		}

		return project.getId();

	}

	 
	 
}
