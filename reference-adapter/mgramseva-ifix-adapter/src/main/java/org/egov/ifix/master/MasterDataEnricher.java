package org.egov.ifix.master;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.master.fetcher.DepartmentEntityFetcher;
import org.egov.ifix.master.fetcher.DepartmentFetcher;
import org.egov.ifix.master.fetcher.ExpenditureFetcher;
import org.egov.ifix.master.fetcher.ProjectFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MasterDataEnricher {

    @Autowired
    DepartmentEntityFetcher departmentEntityFetcher;
    @Autowired
    ProjectFetcher projectFetcher;
    @Autowired
    ExpenditureFetcher expenditureFetcher;
    @Autowired
    DepartmentFetcher departmentFetcher;
    @Autowired
    ObjectMapper objectMapper;

    public final ObjectNode getMasterDataForProjectCode(String departmentEntityCode) {
        // 1 - Get Department Entity Details (based on departmentEntityCode)

        ObjectNode departmentEntityDetails =
                departmentEntityFetcher.getDepartmentEntityDetailsFromCode(departmentEntityCode);

        String departmentUuid = departmentEntityDetails.get("departmentId").asText();
        departmentEntityDetails.remove("departmentId");

        // 2 - Get Project Details (based on department entity uuid)
        String departmentEntityUuid = departmentEntityDetails.get("id").asText();
        ObjectNode projectDetails = projectFetcher.getProjectDetailsOfDepartmentEntity(departmentEntityUuid);

        String expenditureUuid = projectDetails.get("expenditureId").asText();
        projectDetails.remove("expenditureId");

        // 3 - Get Expenditure Details (based on expenditure uuid extracted from project details)
        ObjectNode expenditureDetails = expenditureFetcher.getExpenditureDetails(expenditureUuid);

        // 4 - Get Department Details (based on department uuid extracted from department entity details)
        ObjectNode departmentDetails = departmentFetcher.getDepartmentDetails(departmentUuid);

        // 5 - Add all these details to a json object
        ObjectNode additionalAttributes = objectMapper.createObjectNode();
        additionalAttributes.set("departmentEntity", departmentEntityDetails);
        additionalAttributes.set("project", projectDetails);
        additionalAttributes.set("expenditure", expenditureDetails);
        additionalAttributes.set("department", departmentDetails);

        return additionalAttributes;
    }

}
