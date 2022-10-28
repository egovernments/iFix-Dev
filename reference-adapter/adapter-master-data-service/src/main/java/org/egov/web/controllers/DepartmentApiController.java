package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.service.DepartmentService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.Department;
import org.egov.web.models.DepartmentRequest;
import org.egov.web.models.DepartmentResponse;
import org.egov.web.models.DepartmentSearchRequest;
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

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Controller
@RequestMapping("/department/v1")
public class DepartmentApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ResponseHeaderCreator responseHeaderCreator;

    @Autowired
    public DepartmentApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    /**
     * @param body
     * @return
     */
    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<DepartmentResponse> departmentV1CreatePost(@ApiParam(value = "Details for the new department" +
            " RequestHeader (meta data of the API).", required = true) @Valid @RequestBody DepartmentRequest body) {

        DepartmentRequest departmentRequest = departmentService.createDepartment(body);

        ResponseHeader responseHeader = responseHeaderCreator
                .createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);

        DepartmentResponse departmentResponse = DepartmentResponse.builder().responseHeader(responseHeader)
                .department(Collections.singletonList(departmentRequest.getDepartment())).build();

        return new ResponseEntity<DepartmentResponse>(departmentResponse, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<DepartmentResponse> departmentV1SearchPost(@ApiParam(value = "Details for the department " +
            "search criteria + RequestHeader (meta data of the API).",
            required = true) @Valid @RequestBody DepartmentSearchRequest body) {

        List<Department> departments = departmentService.departmentV1SearchPost(body);
        ResponseHeader responseHeader = responseHeaderCreator
                .createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);
        DepartmentResponse departmentResponse = DepartmentResponse.builder().responseHeader(responseHeader)
                .department(departments).build();
        return new ResponseEntity<DepartmentResponse>(departmentResponse, HttpStatus.OK);
    }

}
