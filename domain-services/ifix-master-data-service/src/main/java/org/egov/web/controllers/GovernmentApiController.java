package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.service.GovernmentService;
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
import java.util.Collections;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Controller
@RequestMapping("/government/v1")
public class GovernmentApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private GovernmentService governmentService;

    @Autowired
    private ResponseHeaderCreator responseHeaderCreator;

    @Autowired
    public GovernmentApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<GovernmentResponse> governmentV1CreatePost(@ApiParam(value =
            "Details for the governmet master data creation, RequestHeader (meta data of the API).", required = true)
             @Valid @RequestBody GovernmentRequest body) {

        GovernmentRequest governmentRequest = governmentService.addGovernment(body);

        ResponseHeader responseHeader = responseHeaderCreator.createResponseInfoFromRequestInfo(body.getRequestHeader(), true);

        GovernmentResponse governmentResponse = GovernmentResponse.builder().responseHeader(responseHeader)
                .government(Collections.singletonList(governmentRequest.getGovernment())).build();

        return new ResponseEntity<GovernmentResponse>(governmentResponse, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<GovernmentResponse> governmentV1SearchPost(@ApiParam(value = "RequestHeader meta data.", required = true) @Valid @RequestBody GovernmentSearchRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("")) {
            try {
                return new ResponseEntity<GovernmentResponse>(objectMapper.readValue("", GovernmentResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<GovernmentResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<GovernmentResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}
