package org.egov.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.AuditDetails;
import org.egov.common.contract.request.RequestHeader;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentEntityUtil;
import org.egov.web.models.DepartmentHierarchyLevel;
import org.egov.web.models.DepartmentHierarchyLevelRequest;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DepartmentHierarchyLevelEnrichmentService {

    @Autowired
    private DepartmentEntityUtil departmentEntityUtil;

    @Autowired
    private DepartmentHierarchyLevelRepository levelRepository;

    /**
     * Enrich the Department hierarchy level request
     *
     * @param request
     */
    public void enrichHierarchyLevelCreatePost(DepartmentHierarchyLevelRequest request) {
        RequestHeader requestHeader = request.getRequestHeader();
        DepartmentHierarchyLevel departmentHierarchyLevel = request.getDepartmentHierarchyLevel();

        AuditDetails auditDetails = null;
        if (departmentHierarchyLevel.getAuditDetails() == null) {
            auditDetails = departmentEntityUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), departmentHierarchyLevel.getAuditDetails(), true);
        } else {
            auditDetails = departmentEntityUtil.enrichAuditDetails(requestHeader.getUserInfo().getUuid(), departmentHierarchyLevel.getAuditDetails(), false);
        }

        if(StringUtils.isBlank(departmentHierarchyLevel.getParent())){
            departmentHierarchyLevel.setLevel(0);
            departmentHierarchyLevel.setParent(null);
        }
        enrichDepartmentLevel(departmentHierarchyLevel);

        departmentHierarchyLevel.setId(UUID.randomUUID().toString());
        departmentHierarchyLevel.setAuditDetails(auditDetails);
    }

    private void enrichDepartmentLevel(DepartmentHierarchyLevel departmentHierarchyLevel) {
        if (StringUtils.isNotBlank(departmentHierarchyLevel.getParent()) && StringUtils.isNotBlank(departmentHierarchyLevel.getTenantId())) {
            DepartmentHierarchyLevelSearchCriteria searchCriteria = new DepartmentHierarchyLevelSearchCriteria();
            searchCriteria.setIds(Collections.singletonList(departmentHierarchyLevel.getParent()));
            searchCriteria.setTenantId(departmentHierarchyLevel.getTenantId());

            List<DepartmentHierarchyLevel> dbDepartmentHierarchyLevels = levelRepository.searchDeptHierarchyLevel(searchCriteria);
            //Rare scenario - may be in case of corrupted data
            if (dbDepartmentHierarchyLevels == null || dbDepartmentHierarchyLevels.isEmpty()) {
                throw new CustomException("INVALID_PARENT_ID", "Given parent doesn't exist in the system");
            }
            //set the hierarchy level
            if (dbDepartmentHierarchyLevels.get(0).getLevel() != null) {
                departmentHierarchyLevel.setLevel(dbDepartmentHierarchyLevels.get(0).getLevel() + 1);
            }
        }
    }
}
