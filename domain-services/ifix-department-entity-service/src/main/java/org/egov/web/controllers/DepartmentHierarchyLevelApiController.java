package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
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

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-23T11:51:49.710+05:30")

@Controller
@RequestMapping("/departmentEntity/hierarchyLevel/v1")
public class DepartmentHierarchyLevelApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    public DepartmentHierarchyLevelApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<DepartmentHierarchyLevelResponse> departmentEntityHierarchyLevelV1CreatePost(@ApiParam(value = "Details for the new DepartmentHierarchyLevel + RequestHeader (meta data of the API).", required = true) @Valid @RequestBody DepartmentHierarchyLevelRequest body) {

        return new ResponseEntity<DepartmentHierarchyLevelResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<DepartmentHierarchyLevelResponse> departmentEntityHierarchyLevelV1SearchPost(@ApiParam(value = "RequestHeader meta data.", required = true) @Valid @RequestBody DepartmentHierarchyLevelSearchRequest body) {
        return new ResponseEntity<DepartmentHierarchyLevelResponse>(HttpStatus.NOT_IMPLEMENTED);
    }
}
