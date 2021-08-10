package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.repository.ProjectRepository;
import org.egov.service.ProjectService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Controller
@RequestMapping("/project/v1")
public class ProjectApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ResponseHeaderCreator responseHeaderCreator;

    @Autowired
    public ProjectApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<ProjectResponse> projectV1CreatePost(@ApiParam(value = "Details for the new Project " +
            "RequestHeader (meta data of the API).", required = true) @Valid @RequestBody ProjectRequest body) {

        return new ResponseEntity<ProjectResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<ProjectResponse> projectV1SearchPost(@ApiParam(value = "Details for the Project search " +
            "criteria RequestHeader (meta data of the API).", required = true)
                                                                   @Valid @RequestBody ProjectSearchRequest body) {

        List<Project> projectList = projectService.findAllByCriteria(body);

        ResponseHeader responseHeader = responseHeaderCreator
                .createResponseHeaderFromRequestHeader(body.getRequestHeader(),true);

        ProjectResponse projectResponse = ProjectResponse.builder().responseHeader(responseHeader)
                .project(projectList).build();

        return new ResponseEntity<>(projectResponse, HttpStatus.ACCEPTED);
    }

}
