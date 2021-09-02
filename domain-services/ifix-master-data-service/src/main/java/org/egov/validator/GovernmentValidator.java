package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.repository.GovernmentRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.MasterDataConstants;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.egov.web.models.GovernmentSearchCriteria;
import org.egov.web.models.GovernmentSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GovernmentValidator {

    @Autowired
    GovernmentRepository governmentRepository;

    public void validateGovernmentRequestData(GovernmentRequest governmentRequest) {
        if (governmentRequest != null && governmentRequest.getGovernment() != null
                && governmentRequest.getRequestHeader() != null) {

            RequestHeader requestHeader = governmentRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                throw new CustomException(MasterDataConstants.USER_INFO, "User information is missing");
            }

            Government government = governmentRequest.getGovernment();
            if (StringUtils.isEmpty(government.getId())) {
                throw new CustomException(MasterDataConstants.GOVERNMENT_ID, "Government Id (Tenant id) is missing in request data");
            }else if (government.getId().length() < 1 || government.getId().length() > 64) {
                throw new CustomException(MasterDataConstants.GOVERNMENT_ID, "Government id length is invalid.");
            }

            if (StringUtils.isEmpty(government.getName())) {
                throw new CustomException(MasterDataConstants.GOVERNMENT_NAME, "Government name is missing in request data");
            }else if (government.getName().length() < 2 || government.getName().length() > 256) {
                throw new CustomException(MasterDataConstants.GOVERNMENT_NAME, "Government name length is invalid");
            }

            Government existingGovernment = governmentRepository.findById(government.getId());
            if (existingGovernment != null) {
                throw new CustomException(MasterDataConstants.GOVERNMENT_ID, "Duplicate government id");
            }
        }else {
            throw new CustomException(MasterDataConstants.REQUEST_PAYLOAD_MISSING, "Request payload is missing some value");
        }
    }

    public void validateGovernmentSearchRequestData(GovernmentSearchRequest governmentSearchRequest) {
        if (governmentSearchRequest != null && governmentSearchRequest.getRequestHeader() != null
                && governmentSearchRequest.getCriteria() != null) {

            GovernmentSearchCriteria criteria = governmentSearchRequest.getCriteria();

            if (criteria == null) {
                throw new CustomException("INVALID_SEARCH_CRITERIA", "Search criteria is missing");
            }else if (criteria.getIds() == null || criteria.getIds().isEmpty()) {
                throw new CustomException(MasterDataConstants.GOVERNMENT_ID, "Government id list are missing");
            }

        } else {
            throw new CustomException(MasterDataConstants.REQUEST_PAYLOAD_MISSING, "Request payload is missing some value");
        }
    }
}
