package org.egov.ifix.service.impl;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.cache.AdapterCache;
import org.egov.ifix.exception.HttpCustomException;
import org.egov.ifix.models.ErrorDataModel;
import org.egov.ifix.models.Project;
import org.egov.ifix.repository.ProjectRepository;
import org.egov.ifix.service.ProjectService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.egov.ifix.utils.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.egov.ifix.utils.EventConstants.LOG_INFO_PREFIX;
import static org.egov.ifix.utils.EventConstants.PROJECT_CODE;

/**
 * @author mani
 * This will manage getting the right Project in running
 * environment. This will cache the data once received from repository.
 * Expiry is not set it will be global config which will clear cache on
 * specified time. It depends on repository to provide project object for
 * given code.
 * <p>
 * Update:
 *         TODO: Expiry time of cached data in Redis server.
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AdapterCache<Project> projectCache;

    @Autowired
    private DataWrapper dataWrapper;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * @param projectCode
     * @param jsonObjectData
     * @return
     */
    @Override
    public String getResolvedProjectId(String projectCode, JsonObject jsonObjectData) {

        Project project = projectCache.getValue(projectCode);

        if (project == null) {
            Optional<Project> projectOptional = projectRepository
                    .getProjectFromIFixMasterService(projectCode, jsonObjectData);

            if (projectOptional.isPresent()) {
                projectCache.putValue(projectCode, projectOptional.get());
                project = projectOptional.get();
            } else {
//TODO: Error handling stream.
/*
                Optional<ErrorDataModel> errorDataModelOptional = dataWrapper.getErrorDataModel(NA,
                        PROJECT_CODE_DATA_NAME, CLIENT_PROJECT_CODE, projectCode, NON_RECOVERABLE_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        "Unable to receive project id corresponding to project code");

                if (errorDataModelOptional.isPresent()) {
                    kafkaTemplate.send(applicationConfiguration.getErrorTopicName(), errorDataModelOptional.get());
                }
*/
                throw new HttpCustomException(PROJECT_CODE, "Unable to find project by client project code",
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            log.info(LOG_INFO_PREFIX + "Project from cache" + project);
        }
        return project.getId();
    }

}
