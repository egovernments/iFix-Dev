package org.egov.ifix.aggregate.repository.mapper;


import org.springframework.stereotype.Component;

@Component
public class FiscalEventAggregateQuery {

    public static final String UPSERT_QUERY_FOR_FISCAL_EVENT_AGGREGATE = "INSERT INTO fiscal_event_aggregated" +
            "(ver,tenantId,government_id,government_name,type,sumAmount,fiscalPeriod,count,attributes_department_id,attributes_department_code," +
            "attributes_department_name,attributes_expenditure_id,attributes_expenditure_code,attributes_expenditure_name,attributes_expenditure_type,attributes_project_id,attributes_project_code,attributes_project_name,coa_id,coa_coaCode,coa_majorHead,coa_majorHeadName," +
            "coa_majorHeadType,coa_subMajorHead,coa_subMajorHeadName,coa_minorHead,coa_minorHeadName,coa_subHead,coa_subHeadName," +
            "coa_groupHead,coa_groupHeadName,coa_objectHead,coa_objectHeadName," +
            "attributes_departmentEntity_ancestry_0_id,attributes_departmentEntity_ancestry_0_code,attributes_departmentEntity_ancestry_0_name," +
            "attributes_departmentEntity_ancestry_0_hierarchyLevel,attributes_departmentEntity_ancestry_1_id,attributes_departmentEntity_ancestry_1_code," +
            "attributes_departmentEntity_ancestry_1_name,attributes_departmentEntity_ancestry_1_hierarchyLevel,attributes_departmentEntity_ancestry_2_id," +
            "attributes_departmentEntity_ancestry_2_code,attributes_departmentEntity_ancestry_2_name,attributes_departmentEntity_ancestry_2_hierarchyLevel," +
            "attributes_departmentEntity_ancestry_3_id,attributes_departmentEntity_ancestry_3_code,attributes_departmentEntity_ancestry_3_name," +
            "attributes_departmentEntity_ancestry_3_hierarchyLevel,attributes_departmentEntity_ancestry_4_id,attributes_departmentEntity_ancestry_4_code," +
            "attributes_departmentEntity_ancestry_4_name,attributes_departmentEntity_ancestry_4_hierarchyLevel,attributes_departmentEntity_ancestry_5_id," +
            "attributes_departmentEntity_ancestry_5_code,attributes_departmentEntity_ancestry_5_name,attributes_departmentEntity_ancestry_5_hierarchyLevel," +
            "attributes_departmentEntity_ancestry_6_id,attributes_departmentEntity_ancestry_6_code,attributes_departmentEntity_ancestry_6_name," +
            "attributes_departmentEntity_ancestry_6_hierarchyLevel,attributes_departmentEntity_ancestry_7_id,attributes_departmentEntity_ancestry_7_code," +
            "attributes_departmentEntity_ancestry_7_name,attributes_departmentEntity_ancestry_7_hierarchyLevel,attributes_departmentEntity_ancestry_8_id," +
            "attributes_departmentEntity_ancestry_8_code,attributes_departmentEntity_ancestry_8_name,attributes_departmentEntity_ancestry_8_hierarchyLevel," +
            "attributes_departmentEntity_ancestry_9_id,attributes_departmentEntity_ancestry_9_code,attributes_departmentEntity_ancestry_9_name," +
            "attributes_departmentEntity_ancestry_9_hierarchyLevel,attributes_departmentEntity_ancestry_10_id,attributes_departmentEntity_ancestry_10_code," +
            "attributes_departmentEntity_ancestry_10_name,attributes_departmentEntity_ancestry_10_hierarchyLevel,attributes_departmentEntity_id,attributes_departmentEntity_code," +
            "attributes_departmentEntity_name,attributes_departmentEntity_hierarchyLevel) " +
            "VALUES" +
            "(:ver,:tenantId,:government_id,:government_name,:type,:sumAmount,:fiscalPeriod,:count," +
            ":attributes_department_id,:attributes_department_code,:attributes_department_name,:attributes_expenditure_id,:attributes_expenditure_code,:attributes_expenditure_name," +
            ":attributes_expenditure_type,:attributes_project_id,:attributes_project_code,:attributes_project_name," +
            ":coa_id,:coa_coaCode,:coa_majorHead,:coa_majorHeadName,:coa_majorHeadType,:coa_subMajorHead,:coa_subMajorHeadName," +
            ":coa_minorHead,:coa_minorHeadName,:coa_subHead,:coa_subHeadName,:coa_groupHead,:coa_groupHeadName,:coa_objectHead," +
            ":coa_objectHeadName,:attributes_departmentEntity_ancestry_0_id,:attributes_departmentEntity_ancestry_0_code,:attributes_departmentEntity_ancestry_0_name," +
            ":attributes_departmentEntity_ancestry_0_hierarchyLevel,:attributes_departmentEntity_ancestry_1_id,:attributes_departmentEntity_ancestry_1_code," +
            ":attributes_departmentEntity_ancestry_1_name,:attributes_departmentEntity_ancestry_1_hierarchyLevel,:attributes_departmentEntity_ancestry_2_id," +
            ":attributes_departmentEntity_ancestry_2_code,:attributes_departmentEntity_ancestry_2_name,:attributes_departmentEntity_ancestry_2_hierarchyLevel," +
            ":attributes_departmentEntity_ancestry_3_id,:attributes_departmentEntity_ancestry_3_code,:attributes_departmentEntity_ancestry_3_name," +
            ":attributes_departmentEntity_ancestry_3_hierarchyLevel,:attributes_departmentEntity_ancestry_4_id,:attributes_departmentEntity_ancestry_4_code," +
            ":attributes_departmentEntity_ancestry_4_name,:attributes_departmentEntity_ancestry_4_hierarchyLevel,:attributes_departmentEntity_ancestry_5_id," +
            ":attributes_departmentEntity_ancestry_5_code,:attributes_departmentEntity_ancestry_5_name,:attributes_departmentEntity_ancestry_5_hierarchyLevel," +
            ":attributes_departmentEntity_ancestry_6_id,:attributes_departmentEntity_ancestry_6_code,:attributes_departmentEntity_ancestry_6_name," +
            ":attributes_departmentEntity_ancestry_6_hierarchyLevel,:attributes_departmentEntity_ancestry_7_id,:attributes_departmentEntity_ancestry_7_code," +
            ":attributes_departmentEntity_ancestry_7_name,:attributes_departmentEntity_ancestry_7_hierarchyLevel,:attributes_departmentEntity_ancestry_8_id," +
            ":attributes_departmentEntity_ancestry_8_code,:attributes_departmentEntity_ancestry_8_name,:attributes_departmentEntity_ancestry_8_hierarchyLevel," +
            ":attributes_departmentEntity_ancestry_9_id,:attributes_departmentEntity_ancestry_9_code,:attributes_departmentEntity_ancestry_9_name," +
            ":attributes_departmentEntity_ancestry_9_hierarchyLevel,:attributes_departmentEntity_ancestry_10_id,:attributes_departmentEntity_ancestry_10_code," +
            ":attributes_departmentEntity_ancestry_10_name,:attributes_departmentEntity_ancestry_10_hierarchyLevel," +
            ":attributes_departmentEntity_id,:attributes_departmentEntity_code,:attributes_departmentEntity_name,:attributes_departmentEntity_hierarchyLevel) " +
            "ON CONFLICT(attributes_departmentEntity_ancestry_6_id,coa_id,type,fiscalPeriod) " +
            "DO UPDATE " +
            "SET " +
            "ver=:ver,tenantId=:tenantId,government_id=:government_id,government_name=:government_name,sumAmount=:sumAmount,count=:count," +
            "attributes_project_id=:attributes_project_id,attributes_department_id=:attributes_department_id,attributes_department_code=:attributes_department_code,attributes_department_name=:attributes_department_name,attributes_departmentEntity_ancestry_0_id=:attributes_departmentEntity_ancestry_0_id," +
            "attributes_expenditure_id=:attributes_expenditure_id,attributes_expenditure_code=:attributes_expenditure_code,attributes_expenditure_name=:attributes_expenditure_name,attributes_expenditure_type=:attributes_expenditure_type," +
            "attributes_project_code=:attributes_project_code,attributes_project_name=:attributes_project_name," +
            "coa_coaCode=:coa_coaCode,coa_majorHead=:coa_majorHead,coa_majorHeadName=:coa_majorHeadName,coa_majorHeadType=:coa_majorHeadType,coa_subMajorHead=:coa_subMajorHead," +
            "coa_subMajorHeadName=:coa_subMajorHeadName,coa_minorHead=:coa_minorHead,coa_minorHeadName=:coa_minorHeadName,coa_subHead=:coa_subHead," +
            "coa_subHeadName=:coa_subHeadName,coa_groupHead=:coa_groupHead,coa_groupHeadName=:coa_groupHeadName,coa_objectHead=:coa_objectHead,coa_objectHeadName=:coa_objectHeadName, " +
            "attributes_departmentEntity_ancestry_0_code=:attributes_departmentEntity_ancestry_0_code," +
            "attributes_departmentEntity_ancestry_0_name=:attributes_departmentEntity_ancestry_0_name,attributes_departmentEntity_ancestry_0_hierarchyLevel=:attributes_departmentEntity_ancestry_0_hierarchyLevel,attributes_departmentEntity_ancestry_1_id=:attributes_departmentEntity_ancestry_1_id," +
            "attributes_departmentEntity_ancestry_1_code=:attributes_departmentEntity_ancestry_1_code,attributes_departmentEntity_ancestry_1_name=:attributes_departmentEntity_ancestry_1_name,attributes_departmentEntity_ancestry_1_hierarchyLevel=:attributes_departmentEntity_ancestry_1_hierarchyLevel," +
            "attributes_departmentEntity_ancestry_2_id=:attributes_departmentEntity_ancestry_2_id,attributes_departmentEntity_ancestry_2_code=:attributes_departmentEntity_ancestry_2_code,attributes_departmentEntity_ancestry_2_name=:attributes_departmentEntity_ancestry_2_name," +
            "attributes_departmentEntity_ancestry_2_hierarchyLevel=:attributes_departmentEntity_ancestry_2_hierarchyLevel,attributes_departmentEntity_ancestry_3_id=:attributes_departmentEntity_ancestry_3_id,attributes_departmentEntity_ancestry_3_code=:attributes_departmentEntity_ancestry_3_code," +
            "attributes_departmentEntity_ancestry_3_name=:attributes_departmentEntity_ancestry_3_name,attributes_departmentEntity_ancestry_3_hierarchyLevel=:attributes_departmentEntity_ancestry_3_hierarchyLevel,attributes_departmentEntity_ancestry_4_id=:attributes_departmentEntity_ancestry_4_id," +
            "attributes_departmentEntity_ancestry_4_code=:attributes_departmentEntity_ancestry_4_code,attributes_departmentEntity_ancestry_4_name=:attributes_departmentEntity_ancestry_4_name,attributes_departmentEntity_ancestry_4_hierarchyLevel=:attributes_departmentEntity_ancestry_4_hierarchyLevel," +
            "attributes_departmentEntity_ancestry_5_id=:attributes_departmentEntity_ancestry_5_id,attributes_departmentEntity_ancestry_5_code=:attributes_departmentEntity_ancestry_5_code,attributes_departmentEntity_ancestry_5_name=:attributes_departmentEntity_ancestry_5_name," +
            "attributes_departmentEntity_ancestry_5_hierarchyLevel=:attributes_departmentEntity_ancestry_5_hierarchyLevel,attributes_departmentEntity_ancestry_6_id=:attributes_departmentEntity_ancestry_6_id,attributes_departmentEntity_ancestry_6_code=:attributes_departmentEntity_ancestry_6_code," +
            "attributes_departmentEntity_ancestry_6_name=:attributes_departmentEntity_ancestry_6_name,attributes_departmentEntity_ancestry_6_hierarchyLevel=:attributes_departmentEntity_ancestry_6_hierarchyLevel,attributes_departmentEntity_ancestry_7_id=:attributes_departmentEntity_ancestry_7_id," +
            "attributes_departmentEntity_ancestry_7_code=:attributes_departmentEntity_ancestry_7_code,attributes_departmentEntity_ancestry_7_name=:attributes_departmentEntity_ancestry_7_name,attributes_departmentEntity_ancestry_7_hierarchyLevel=:attributes_departmentEntity_ancestry_7_hierarchyLevel," +
            "attributes_departmentEntity_ancestry_8_id=:attributes_departmentEntity_ancestry_8_id,attributes_departmentEntity_ancestry_8_code=:attributes_departmentEntity_ancestry_8_code,attributes_departmentEntity_ancestry_8_name=:attributes_departmentEntity_ancestry_8_name," +
            "attributes_departmentEntity_ancestry_8_hierarchyLevel=:attributes_departmentEntity_ancestry_8_hierarchyLevel,attributes_departmentEntity_ancestry_9_id=:attributes_departmentEntity_ancestry_9_id,attributes_departmentEntity_ancestry_9_code=:attributes_departmentEntity_ancestry_9_code," +
            "attributes_departmentEntity_ancestry_9_name=:attributes_departmentEntity_ancestry_9_name,attributes_departmentEntity_ancestry_9_hierarchyLevel=:attributes_departmentEntity_ancestry_9_hierarchyLevel,attributes_departmentEntity_ancestry_10_id=:attributes_departmentEntity_ancestry_10_id," +
            "attributes_departmentEntity_ancestry_10_code=:attributes_departmentEntity_ancestry_10_code,attributes_departmentEntity_ancestry_10_name=:attributes_departmentEntity_ancestry_10_name,attributes_departmentEntity_ancestry_10_hierarchyLevel=:attributes_departmentEntity_ancestry_10_hierarchyLevel," +
            "attributes_departmentEntity_id=:attributes_departmentEntity_id,attributes_departmentEntity_code=:attributes_departmentEntity_code,attributes_departmentEntity_name=:attributes_departmentEntity_name,attributes_departmentEntity_hierarchyLevel=:attributes_departmentEntity_hierarchyLevel;";

}
