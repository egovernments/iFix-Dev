package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.GovernmentConstants;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.springframework.stereotype.Component;

@Component
public class GovernmentValidator {

    public void validateGovernmentRequestData(GovernmentRequest governmentRequest) {
        if (governmentRequest != null && governmentRequest.getGovernment() != null
                && governmentRequest.getRequestHeader() != null) {

            RequestHeader requestHeader = governmentRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                throw new CustomException(GovernmentConstants.USER_INFO, "User information is missing");
            }

            Government government = governmentRequest.getGovernment();
            if (StringUtils.isEmpty(government.getId())) {
                throw new CustomException(GovernmentConstants.GOVERNMENT_ID, "Government Id (Tenant id) is missing in request data");
            }

            if (StringUtils.isEmpty(government.getName())) {
                throw new CustomException(GovernmentConstants.GOVERNMENT_NAME, "Government name is missing in request data");
            }

        }else {
            throw new CustomException(GovernmentConstants.REQUEST_PAYLOAD_MISSING, "Request payload is missing some value");
        }
    }
}
