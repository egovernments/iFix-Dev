package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.service.DepartmentHierarchyLevelService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.DepartmentHierarchyLevel;
import org.egov.web.models.DepartmentHierarchyLevelRequest;
import org.egov.web.models.DepartmentHierarchyLevelResponse;
import org.egov.web.models.DepartmentHierarchyLevelSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-23T11:51:49.710+05:30")

@Controller
@RequestMapping("/departmentEntity/hierarchyLevel/v1")
public class DepartmentHierarchyLevelApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private ResponseHeaderCreator responseHeaderCreator;

    @Autowired
    private DepartmentHierarchyLevelService hierarchyLevelService;


    @Autowired
    public DepartmentHierarchyLevelApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<DepartmentHierarchyLevelResponse> departmentEntityHierarchyLevelV1CreatePost(@ApiParam(value = "Details for the new DepartmentHierarchyLevel + RequestHeader (meta data of the API).", required = true) @Valid @RequestBody DepartmentHierarchyLevelRequest body) {
        DepartmentHierarchyLevelRequest departmentHierarchyLevelRequest = hierarchyLevelService.createDepartmentEntityHierarchyLevel(body);
        ResponseHeader responseHeader = responseHeaderCreator.createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);
        DepartmentHierarchyLevelResponse departmentHierarchyLevelResponse = DepartmentHierarchyLevelResponse.builder().responseHeader(responseHeader)
                .departmentHierarchyLevel(Collections.singletonList(departmentHierarchyLevelRequest.getDepartmentHierarchyLevel())).build();
        return new ResponseEntity<DepartmentHierarchyLevelResponse>(departmentHierarchyLevelResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<DepartmentHierarchyLevelResponse> departmentEntityHierarchyLevelV1SearchPost(@ApiParam(value = "RequestHeader meta data.", required = true) @Valid @RequestBody DepartmentHierarchyLevelSearchRequest body) {
        List<DepartmentHierarchyLevel> departmentHierarchyLevels = hierarchyLevelService.searchDepartmentEntityHierarchyLevel(body);
        ResponseHeader responseHeader = responseHeaderCreator.createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);
        DepartmentHierarchyLevelResponse departmentHierarchyLevelResponse = DepartmentHierarchyLevelResponse.builder().responseHeader(responseHeader)
                .departmentHierarchyLevel(departmentHierarchyLevels).build();
        return new ResponseEntity<DepartmentHierarchyLevelResponse>(departmentHierarchyLevelResponse, HttpStatus.OK);
    }
}
