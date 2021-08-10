package org.egov.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.util.MasterDataConstants;
import org.egov.web.models.ProjectSearchCriteria;
import org.egov.web.models.ProjectSearchRequest;
import org.springframework.stereotype.Component;

@Component
public class ProjectValidator {

    public void validateProjectSearchRequest(ProjectSearchRequest projectSearchRequest) {
        if (projectSearchRequest != null && projectSearchRequest.getRequestHeader() != null
                && projectSearchRequest.getCriteria() != null) {

            RequestHeader requestHeader = projectSearchRequest.getRequestHeader();

            if (requestHeader.getUserInfo() == null || StringUtils.isEmpty(requestHeader.getUserInfo().getUuid())) {
                throw new CustomException(MasterDataConstants.USER_INFO, "User information is missing");
            }

            ProjectSearchCriteria projectSearchCriteria = projectSearchRequest.getCriteria();

            if (StringUtils.isEmpty(projectSearchCriteria.getTenantId())) {
                throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id is missing in request data");
            }

            if (projectSearchCriteria.getTenantId().length() < 2 || projectSearchCriteria.getTenantId().length() > 64) {
                throw new CustomException(MasterDataConstants.TENANT_ID, "Tenant id length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getName())
                    && (projectSearchCriteria.getName().length() < 2 || projectSearchCriteria.getName().length() > 256)) {
                throw new CustomException(MasterDataConstants.PROJECT_NAME, "Project name length is invalid. " +
                        "Length range [2-256]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getCode())
                    && (projectSearchCriteria.getCode().length() < 2 || projectSearchCriteria.getCode().length() > 64)) {
                throw new CustomException(MasterDataConstants.PROJECT_CODE, "Project code length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getEatId())
                    && (projectSearchCriteria.getEatId().length() < 2 || projectSearchCriteria.getEatId().length() > 64)) {
                throw new CustomException(MasterDataConstants.EAT_ID, "EAT id length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getDepartmentId())
                    && (projectSearchCriteria.getDepartmentId().length() < 2
                    || projectSearchCriteria.getDepartmentId().length() > 64)) {

                throw new CustomException(MasterDataConstants.DEPARTMENT_ID, "Department id length is invalid. " +
                        "Length range [2-64]");
            }

            if (!StringUtils.isEmpty(projectSearchCriteria.getLocationId())
                    && (projectSearchCriteria.getLocationId().length() < 2
                    || projectSearchCriteria.getLocationId().length() > 64)) {

                throw new CustomException(MasterDataConstants.LOCATION_ID, "Location id length is invalid. " +
                        "Length range [2-64]");
            }
        }
    }
}
