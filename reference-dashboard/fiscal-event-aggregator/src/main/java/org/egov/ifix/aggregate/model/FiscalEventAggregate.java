package org.egov.ifix.aggregate.model;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class FiscalEventAggregate {
    private String ver;
    private String tenantId;
    private String government_id;
    private String government_name;
    private String type;
    private BigDecimal sumAmount;
    private String fiscalPeriod;
    private BigInteger count;
    private String attributes_department_id;
    private String attributes_department_code;
    private String attributes_department_name;

    private String attributes_departmentEntity_id;
    private String attributes_departmentEntity_code;
    private String attributes_departmentEntity_name;
    private Integer attributes_departmentEntity_hierarchyLevel;

    private String attributes_departmentEntity_ancestry_0_id;
    private String attributes_departmentEntity_ancestry_0_code;
    private String attributes_departmentEntity_ancestry_0_name;
    private Integer attributes_departmentEntity_ancestry_0_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_1_id;
    private String attributes_departmentEntity_ancestry_1_code;
    private String attributes_departmentEntity_ancestry_1_name;
    private Integer attributes_departmentEntity_ancestry_1_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_2_id;
    private String attributes_departmentEntity_ancestry_2_code;
    private String attributes_departmentEntity_ancestry_2_name;
    private Integer attributes_departmentEntity_ancestry_2_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_3_id;
    private String attributes_departmentEntity_ancestry_3_code;
    private String attributes_departmentEntity_ancestry_3_name;
    private Integer attributes_departmentEntity_ancestry_3_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_4_id;
    private String attributes_departmentEntity_ancestry_4_code;
    private String attributes_departmentEntity_ancestry_4_name;
    private Integer attributes_departmentEntity_ancestry_4_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_5_id;
    private String attributes_departmentEntity_ancestry_5_code;
    private String attributes_departmentEntity_ancestry_5_name;
    private Integer attributes_departmentEntity_ancestry_5_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_6_id;
    private String attributes_departmentEntity_ancestry_6_code;
    private String attributes_departmentEntity_ancestry_6_name;
    private Integer attributes_departmentEntity_ancestry_6_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_7_id;
    private String attributes_departmentEntity_ancestry_7_code;
    private String attributes_departmentEntity_ancestry_7_name;
    private Integer attributes_departmentEntity_ancestry_7_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_8_id;
    private String attributes_departmentEntity_ancestry_8_code;
    private String attributes_departmentEntity_ancestry_8_name;
    private Integer attributes_departmentEntity_ancestry_8_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_9_id;
    private String attributes_departmentEntity_ancestry_9_code;
    private String attributes_departmentEntity_ancestry_9_name;
    private Integer attributes_departmentEntity_ancestry_9_hierarchyLevel;
    private String attributes_departmentEntity_ancestry_10_id;
    private String attributes_departmentEntity_ancestry_10_code;
    private String attributes_departmentEntity_ancestry_10_name;
    private Integer attributes_departmentEntity_ancestry_10_hierarchyLevel;
    private String attributes_expenditure_id;
    private String attributes_expenditure_code;
    private String attributes_expenditure_name;
    private String attributes_expenditure_type;
    private String attributes_project_id;
    private String attributes_project_code;
    private String attributes_project_name;
    private String coa_id;
    private String coa_coaCode;
    private String coa_majorHead;
    private String coa_majorHeadName;
    private String coa_majorHeadType;
    private String coa_subMajorHead;
    private String coa_subMajorHeadName;
    private String coa_minorHead;
    private String coa_minorHeadName;
    private String coa_subHead;
    private String coa_subHeadName;
    private String coa_groupHead;
    private String coa_groupHeadName;
    private String coa_objectHead;
    private String coa_objectHeadName;
}
