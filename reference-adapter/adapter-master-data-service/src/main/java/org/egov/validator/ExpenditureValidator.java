package org.egov.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.MasterDataConstants;
import org.egov.util.MasterDepartmentUtil;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ExpenditureValidator {

    @Autowired
    private MasterDepartmentUtil masterDepartmentUtil;

    public void validateExpenditureSearchRequest(ExpenditureSearchRequest expenditureSearchRequest) {
        if (expenditureSearchRequest != null || expenditureSearchRequest.getRequestHeader() == null
                && expenditureSearchRequest.getCriteria() != null) {

            RequestHeader requestHeader = expenditureSearchRequest.getRequestHeader();

            ExpenditureSearchCriteria criteria = expenditureSearchRequest.getCriteria();

            if (StringUtils.isEmpty(criteria.getTenantId())) {
                throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id is missing in request data");
            } else if (criteria.getTenantId().length() < 2 || criteria.getTenantId().length() > 64) {
                throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id length is invalid. " +
                        MasterDataConstants.LENGTH_RANGE_2_64);
            }

            if (!StringUtils.isEmpty(criteria.getName())
                    && (criteria.getName().length() < 2 || criteria.getName().length() > 256)) {
                throw new CustomException(MasterDataConstants.EXPENDITURE_NAME, "Expenditure name length is invalid. " +
                        "Length range [2-256]");
            }

            if (!StringUtils.isEmpty(criteria.getCode())
                    && (criteria.getCode().length() < 2 || criteria.getCode().length() > 64)) {
                throw new CustomException(MasterDataConstants.EXPENDITURE_CODE, "Expenditure code length is invalid. " +
                        MasterDataConstants.LENGTH_RANGE_2_64);
            }

        } else {
            throw new CustomException(MasterDataConstants.REQUEST_PAYLOAD_MISSING, "Request payload is missing some value");
        }
    }

    public void validateExpenditureCreateRequest(ExpenditureRequest expenditureRequest) {
        RequestHeader requestHeader = expenditureRequest.getRequestHeader();
        Expenditure expenditure = expenditureRequest.getExpenditure();
        Map<String, String> errorMap = new HashMap<>();

        //Header validation
        if (requestHeader == null) {
            throw new CustomException(MasterDataConstants.REQUEST_HEADER_MISSING, "Request header is missing");
        }
        if (requestHeader.getUserInfo() == null || requestHeader.getUserInfo().getUuid() == null) {
            errorMap.put(MasterDataConstants.USER_INFO, "User info is missing");
        }

        if (expenditure == null) {
            throw new CustomException(MasterDataConstants.REQUEST_PAYLOAD_MISSING, "Expenditure request is invalid");
        }

        //Tenant id
        if (StringUtils.isBlank(expenditure.getTenantId())) {
            errorMap.put(MasterDataConstants.TENANT_ID, "Tenant id is mandatory");
        }
        if (StringUtils.isNotBlank(expenditure.getTenantId())
                && (expenditure.getTenantId().length() < 2 || expenditure.getTenantId().length() > 64)) {
            throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id length is invalid. " +
                    MasterDataConstants.LENGTH_RANGE_2_64);
        }

        //code
        if (StringUtils.isBlank(expenditure.getCode())) {
            errorMap.put(MasterDataConstants.EXPENDITURE_CODE, "Expenditure code is missing in request data");
        }
        if (StringUtils.isNotBlank(expenditure.getCode())
                && (expenditure.getCode().length() < 2 || expenditure.getCode().length() > 64)) {
            errorMap.put(MasterDataConstants.EXPENDITURE_CODE, "Expenditure code length is invalid. " +
                    MasterDataConstants.LENGTH_RANGE_2_64);
        }

        //name
        if (StringUtils.isBlank(expenditure.getName())) {
            errorMap.put(MasterDataConstants.EXPENDITURE_NAME, "Expenditure name is missing in request data");
        }
        if (StringUtils.isNotBlank(expenditure.getName())
                && (expenditure.getName().length() < 2 || expenditure.getName().length() > 256)) {
            errorMap.put(MasterDataConstants.EXPENDITURE_NAME, "Expenditure name length is invalid. " +
                    "Length range [2-256]");
        }

        //type
        if (expenditure.getType() == null) {
            errorMap.put(MasterDataConstants.EXPENDITURE_TYPE, "Expenditure type is missing in request data");
        }
        //department id
        if (StringUtils.isNotBlank(expenditure.getDepartmentId()) && StringUtils.isNotBlank(expenditure.getTenantId())) {
            List<Department> departments = masterDepartmentUtil.fetchDepartment(Collections.singletonList(expenditure.getDepartmentId()), expenditure.getTenantId(), requestHeader);
            if (departments == null || departments.isEmpty()) {
                errorMap.put(MasterDataConstants.DEPARTMENT_ID, "Department id doesn't exist in the system");
            }
        }

        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }
}
