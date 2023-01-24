package org.egov.web.controllers;


import io.swagger.annotations.ApiParam;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.service.ProjectService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.ProjectDTO;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.ProjectResponse;
import org.egov.web.models.ProjectSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Controller
@RequestMapping("/project/v1")
public class ProjectApiController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ResponseHeaderCreator responseHeaderCreator;

    /**
     * @param body
     * @return
     */
    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<ProjectResponse> projectV1CreatePost(@ApiParam(value = "Details for the new ProjectConst " +
            "RequestHeader (meta data of the API).", required = true) @Valid @RequestBody ProjectRequest body) {

        ProjectRequest projectRequest = projectService.createProject(body);

        ResponseHeader responseHeader = responseHeaderCreator
                .createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);

        ProjectResponse projectResponse = ProjectResponse.builder().responseHeader(responseHeader)
                .projectDTO(Collections.singletonList(projectRequest.getProjectDTO())).build();

        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }


    /**
     * @param body
     * @return
     */
    @RequestMapping(value = "/_update", method = RequestMethod.POST)
    public ResponseEntity<ProjectResponse> projectV1UpdatePost(@ApiParam(value = "Details to update the existing ProjectConst " +
            "RequestHeader (meta data of the API).", required = true) @Valid @RequestBody ProjectRequest body) {

        ProjectRequest projectRequest = projectService.updateProject(body);

        ResponseHeader responseHeader = responseHeaderCreator
                .createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);

        ProjectResponse projectResponse = ProjectResponse.builder().responseHeader(responseHeader)
                .projectDTO(Collections.singletonList(projectRequest.getProjectDTO())).build();

        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }

    /**
     * @param body
     * @return
     */
    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<ProjectResponse> projectV1SearchPost(@ApiParam(value = "Details for the ProjectConst search " +
            "criteria RequestHeader (meta data of the API).", required = true)
                                                               @Valid @RequestBody ProjectSearchRequest body) {

        List<ProjectDTO> projectDTOList = projectService.findAllByCriteria(body);

        ResponseHeader responseHeader = responseHeaderCreator
                .createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);

        ProjectResponse projectResponse = ProjectResponse.builder().responseHeader(responseHeader)
                .projectDTO(projectDTOList).build();

        return new ResponseEntity<>(projectResponse, HttpStatus.ACCEPTED);
    }

}
