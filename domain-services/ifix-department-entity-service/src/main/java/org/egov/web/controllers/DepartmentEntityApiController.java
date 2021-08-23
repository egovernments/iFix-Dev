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
import java.io.IOException;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-23T11:51:49.710+05:30")

@Controller
@RequestMapping("/departmentEntity/v1")
public class DepartmentEntityApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    public DepartmentEntityApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<DepartmentEntityResponse> departmentEntityV1CreatePost(@ApiParam(value = "Details for the new Department Entity + RequestHeader (meta data of the API).", required = true) @Valid @RequestBody DepartmentEntityRequest body) {
        return new ResponseEntity<DepartmentEntityResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<DepartmentEntityResponse> departmentEntityV1SearchPost(@ApiParam(value = "RequestHeader meta data.", required = true) @Valid @RequestBody DepartmentEntitySearchRequest body) {
        return new ResponseEntity<DepartmentEntityResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}
