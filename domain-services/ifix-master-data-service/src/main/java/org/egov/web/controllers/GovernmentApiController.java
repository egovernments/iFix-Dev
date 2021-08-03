package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
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
import java.io.IOException;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Controller
@RequestMapping("/eGovTrial/iFIX-Master-Data/1.0.0")
public class GovernmentApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    public GovernmentApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/government/v1/_create", method = RequestMethod.POST)
    public ResponseEntity<GovernmentResponse> governmentV1CreatePost(@ApiParam(value =
            "Details for the governmet master data creation, RequestHeader (meta data of the API).", required = true)
             @Valid @RequestBody GovernmentRequest body) {

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

    @RequestMapping(value = "/government/v1/_search", method = RequestMethod.POST)
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
