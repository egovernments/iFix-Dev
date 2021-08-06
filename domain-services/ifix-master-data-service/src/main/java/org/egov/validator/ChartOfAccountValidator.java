package org.egov.validator;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.CoaUtil;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ChartOfAccountValidator {


    @Autowired
    private CoaUtil coaUtil;

    public void validateCreatePost(COARequest coaRequest) {
        log.info("Enter into ChartOfAccountValidator.validateCreatePost()");
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        RequestHeader requestHeader = coaRequest.getRequestHeader();
        Map<String, String> errorMap = new HashMap<>();

        //Header validation
        if (requestHeader == null) {
            throw new CustomException("REQUEST_HEADER", "Request header is missing");
        }
        if (requestHeader.getUserInfo() == null || requestHeader.getUserInfo().getUuid() == null) {
            errorMap.put("USER_INFO", "User info is missing");
        }

        if (chartOfAccount == null) {
            throw new CustomException("INVALID_REQUEST", "COA request is invalid");
        }

        //code validation
        if (StringUtils.isBlank(chartOfAccount.getGroupHead())) {
            errorMap.put("GROUP_HEAD", "Group head Code is mandatory for chart of account");
        }
        if (StringUtils.isNotBlank(chartOfAccount.getGroupHead()) && chartOfAccount.getGroupHead().length() != 2) {
            errorMap.put("GROUP_HEAD_CODE_LENGTH", "Group head Code should be of length 2");
        }
        if (StringUtils.isBlank(chartOfAccount.getMajorHead())) {
            errorMap.put("MAJOR_HEAD", "Major head Code is mandatory for chart of account");
        }
        if (StringUtils.isNotBlank(chartOfAccount.getMajorHead()) && chartOfAccount.getMajorHead().length() != 4) {
            errorMap.put("MAJOR_HEAD_CODE_LENGTH", "Major head Code should be of length 4");
        }
        if (StringUtils.isBlank(chartOfAccount.getMinorHead())) {
            errorMap.put("MINOR_HEAD", "Minor head Code is mandatory for chart of account");
        }
        if (StringUtils.isNotBlank(chartOfAccount.getMinorHead()) && chartOfAccount.getMinorHead().length() != 3) {
            errorMap.put("MINOR_HEAD_CODE_LENGTH", "Minor head Code should be of length 3");
        }
        if (StringUtils.isBlank(chartOfAccount.getSubHead())) {
            errorMap.put("SUB_HEAD", "Sub head Code is mandatory for chart of account");
        }
        if (StringUtils.isNotBlank(chartOfAccount.getSubHead()) && chartOfAccount.getSubHead().length() != 2) {
            errorMap.put("SUB_HEAD_CODE_LENGTH", "Sub head Code should be of length 2");
        }
        if (StringUtils.isBlank(chartOfAccount.getObjectHead())) {
            errorMap.put("OBJECT_HEAD", "Object head Code is mandatory for chart of account");
        }
        if (StringUtils.isNotBlank(chartOfAccount.getObjectHead()) && chartOfAccount.getObjectHead().length() != 2) {
            errorMap.put("OBJECT_HEAD_CODE_LENGTH", "Object head Code should be of length 2");
        }
        if (StringUtils.isBlank(chartOfAccount.getSubMajorHead())) {
            errorMap.put("SUB_MAJOR_HEAD", "Sub Major Code is mandatory for chart of account");
        }
        if (StringUtils.isNotBlank(chartOfAccount.getSubMajorHead()) && chartOfAccount.getSubMajorHead().length() != 2) {
            errorMap.put("SUB_MAJOR_HEAD_CODE_LENGTH", "Sub major head Code should be of length 2");
        }

        //Code name and type - validation
        if (StringUtils.isNotBlank(chartOfAccount.getMajorHeadName())
                && (chartOfAccount.getMajorHeadName().length() < 2 || chartOfAccount.getMajorHeadName().length() > 64))
            errorMap.put("MAJOR_HEAD_NAME", "Major head name's length is invalid");

        if (StringUtils.isNotBlank(chartOfAccount.getMajorHeadtype())
                && (chartOfAccount.getMajorHeadtype().length() < 2 || chartOfAccount.getMajorHeadtype().length() > 32))
            errorMap.put("MAJOR_HEAD_TYPE", "Major head type's length is invalid");

        if (StringUtils.isNotBlank(chartOfAccount.getSubMajorHeadName())
                && (chartOfAccount.getSubMajorHeadName().length() < 2 || chartOfAccount.getSubMajorHeadName().length() > 64))
            errorMap.put("SUB_MAJOR_HEAD_NAME", "Sub Major head name's length is invalid");

        if (StringUtils.isNotBlank(chartOfAccount.getMinorHeadName())
                && (chartOfAccount.getMinorHeadName().length() < 2 || chartOfAccount.getMinorHeadName().length() > 64))
            errorMap.put("MINOR_HEAD_NAME", "Minor head name's length is invalid");

        if (StringUtils.isNotBlank(chartOfAccount.getSubHeadName())
                && (chartOfAccount.getSubHeadName().length() < 2 || chartOfAccount.getSubHeadName().length() > 64))
            errorMap.put("SUB_HEAD_NAME", "Sub head name's length is invalid");

        if (StringUtils.isNotBlank(chartOfAccount.getGroupHeadName())
                && (chartOfAccount.getGroupHeadName().length() < 2 || chartOfAccount.getGroupHeadName().length() > 64))
            errorMap.put("GROUP_HEAD_NAME", "Group head name's length is invalid");

        if (StringUtils.isNotBlank(chartOfAccount.getObjectHeadName())
                && (chartOfAccount.getObjectHeadName().length() < 2 || chartOfAccount.getObjectHeadName().length() > 64))
            errorMap.put("OBJECT_HEAD_NAME", "Object head name's length is invalid");


        //validate the tenant id is exist in the system or not
        if (StringUtils.isNotBlank(chartOfAccount.getTenantId())) {
            //call the Tenant Service for search, if doesn't exist add in the error map
            List<Government> governments = coaUtil.searchTenants(requestHeader, chartOfAccount);
            if (governments == null || governments.isEmpty()) {
                errorMap.put("TENANT_ID", "Tenant id doesn't exist in the system");
            }
        }


        log.info("Exit from ChartOfAccountValidator.validateCreatePost()");
        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);

    }

    public void validateSearchPost(COASearchRequest coaSearchRequest) {
        log.info("Enter into ChartOfAccountValidator.validateSearchPost()");
        COASearchCriteria searchCriteria = coaSearchRequest.getCriteria();
        RequestHeader requestHeader = coaSearchRequest.getRequestHeader();
        Map<String, String> errorMap = new HashMap<>();

        //Header validation
        if (requestHeader == null) {
            throw new CustomException("REQUEST_HEADER", "Request header is missing");
        }
        if (requestHeader.getUserInfo() == null || requestHeader.getUserInfo().getUuid() == null) {
            errorMap.put("USER_INFO", "User info is missing");
        }

        if (searchCriteria == null) {
            throw new CustomException("INVALID_SEARCH_CRITERIA", "Search criteria is missing");
        }
        //Tenant id validation
        if (StringUtils.isBlank(searchCriteria.getTenantId())) {
            errorMap.put("TENANT_ID", "Tenant id is mandatory");
        }
        if (StringUtils.isNotBlank(searchCriteria.getTenantId())
                && (searchCriteria.getTenantId().length() < 2 || searchCriteria.getTenantId().length() > 64))
            errorMap.put("TENANT_ID_LENGTH", "Tenant id's length is invalid");

        //Code validation
        if (StringUtils.isNotBlank(searchCriteria.getGroupHead()) && searchCriteria.getGroupHead().length() != 2) {
            errorMap.put("GROUP_HEAD_CODE_LENGTH", "Group head Code should be of length 2");
        }
        if (StringUtils.isNotBlank(searchCriteria.getMajorHead()) && searchCriteria.getMajorHead().length() != 4) {
            errorMap.put("MAJOR_HEAD_CODE_LENGTH", "Major head Code should be of length 4");
        }
        if (StringUtils.isNotBlank(searchCriteria.getMinorHead()) && searchCriteria.getMinorHead().length() != 3) {
            errorMap.put("MINOR_HEAD_CODE_LENGTH", "Minor head Code should be of length 3");
        }
        if (StringUtils.isNotBlank(searchCriteria.getSubHead()) && searchCriteria.getSubHead().length() != 2) {
            errorMap.put("SUB_HEAD_CODE_LENGTH", "Sub head Code should be of length 2");
        }
        if (StringUtils.isNotBlank(searchCriteria.getObjectHead()) && searchCriteria.getObjectHead().length() != 2) {
            errorMap.put("OBJECT_HEAD_CODE_LENGTH", "Object head Code should be of length 2");
        }
        if (StringUtils.isNotBlank(searchCriteria.getSubMajorHead()) && searchCriteria.getSubMajorHead().length() != 2) {
            errorMap.put("SUB_MAJOR_HEAD_CODE_LENGTH", "Sub major head Code should be of length 2");
        }
        if (StringUtils.isNotBlank(searchCriteria.getCoaCode()) && (searchCriteria.getCoaCode().length() < 1
                || searchCriteria.getCoaCode().length()>64) ) {
            errorMap.put("SUB_MAJOR_HEAD_CODE_LENGTH", "Sub major head Code should be of length 2");
        }


        log.info("Exit from ChartOfAccountValidator.validateSearchPost()");

        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }
}
