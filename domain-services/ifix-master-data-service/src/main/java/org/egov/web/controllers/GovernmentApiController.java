package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.service.GovernmentService;
import org.egov.util.ResponseHeaderCreator;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.egov.web.models.GovernmentResponse;
import org.egov.web.models.GovernmentSearchRequest;
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

    /**
     * @param body
     * @return
     */
    @RequestMapping(value = "/_create", method = RequestMethod.POST)
    public ResponseEntity<GovernmentResponse> governmentV1CreatePost(@ApiParam(value =
            "Details for the governmet master data creation, RequestHeader (meta data of the API).", required = true)
                                                                     @Valid @RequestBody GovernmentRequest body) {

        GovernmentRequest governmentRequest = governmentService.addGovernment(body);

        ResponseHeader responseHeader = responseHeaderCreator.createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);

        GovernmentResponse governmentResponse = GovernmentResponse.builder().responseHeader(responseHeader)
                .government(Collections.singletonList(governmentRequest.getGovernment())).build();

        return new ResponseEntity<GovernmentResponse>(governmentResponse, HttpStatus.ACCEPTED);
    }

    /**
     * @param body
     * @return
     */
    @RequestMapping(value = "/_search", method = RequestMethod.POST)
    public ResponseEntity<GovernmentResponse> governmentV1SearchPost(@ApiParam(value = "RequestHeader meta data.",
            required = true) @Valid @RequestBody GovernmentSearchRequest body) {

        List<Government> governmentList = governmentService.searchAllGovernmentByIdList(body);

        ResponseHeader responseHeader = responseHeaderCreator
                .createResponseHeaderFromRequestHeader(body.getRequestHeader(), true);

        GovernmentResponse governmentResponse = GovernmentResponse.builder().responseHeader(responseHeader)
                .government(governmentList).build();

        return new ResponseEntity<GovernmentResponse>(governmentResponse, HttpStatus.ACCEPTED);
    }

}
