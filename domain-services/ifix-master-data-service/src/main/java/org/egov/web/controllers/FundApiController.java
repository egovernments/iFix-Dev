package org.egov.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.egov.web.models.FundRequest;
import org.egov.web.models.FundResponse;
import org.egov.web.models.FundSearchRequest;
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
public class FundApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    public FundApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/fund/v1/_create", method = RequestMethod.POST)
    public ResponseEntity<FundResponse> fundV1CreatePost(@ApiParam(value = "Details for the new fund + RequestHeader (meta data of the API).", required = true) @Valid @RequestBody FundRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("")) {
            try {
                return new ResponseEntity<FundResponse>(objectMapper.readValue("", FundResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<FundResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<FundResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/fund/v1/_search", method = RequestMethod.POST)
    public ResponseEntity<FundResponse> fundV1SearchPost(@ApiParam(value = "RequestHeader meta data.", required = true) @Valid @RequestBody FundSearchRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("")) {
            try {
                return new ResponseEntity<FundResponse>(objectMapper.readValue("", FundResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<FundResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<FundResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}
