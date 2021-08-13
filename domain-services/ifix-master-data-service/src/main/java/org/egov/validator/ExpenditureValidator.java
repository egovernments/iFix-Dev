package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.MasterDataConstants;
import org.egov.web.models.ExpenditureSearchCriteria;
import org.egov.web.models.ExpenditureSearchRequest;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureValidator {

    public void validateExpenditureSearchRequest(ExpenditureSearchRequest expenditureSearchRequest) {
        if (expenditureSearchRequest != null || expenditureSearchRequest.getRequestHeader() == null
                && expenditureSearchRequest.getCriteria() != null) {

            RequestHeader requestHeader = expenditureSearchRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                throw new CustomException(MasterDataConstants.USER_INFO, "User information is missing");
            }

            ExpenditureSearchCriteria criteria = expenditureSearchRequest.getCriteria();

            if (StringUtils.isEmpty(criteria.getTenantId())) {
                throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id is missing in request data");
            }

            if (criteria.getTenantId().length() < 2 || criteria.getTenantId().length() > 64) {
                throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(criteria.getName())
                    && (criteria.getName().length() < 2 || criteria.getName().length() > 256)) {
                throw new CustomException(MasterDataConstants.EXPENDITURE_NAME, "Expenditure name length is invalid. " +
                        "Length range [2-256]");
            }

            if (!StringUtils.isEmpty(criteria.getCode())
                    && (criteria.getCode().length() < 2 || criteria.getCode().length() > 64)) {
                    throw new CustomException(MasterDataConstants.EXPENDITURE_CODE, "Expenditure code length is invalid. " +
                            "Length range [2-64]");
            }

        }else {
            throw new CustomException(MasterDataConstants.REQUEST_PAYLOAD_MISSING, "Request payload is missing some value");
        }
    }
}
