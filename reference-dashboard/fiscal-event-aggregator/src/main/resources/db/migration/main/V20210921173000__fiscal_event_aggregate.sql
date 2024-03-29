DROP TABLE IF EXISTS fiscal_event_aggregated;

CREATE TABLE fiscal_event_aggregated(
  ver TEXT,
  id SERIAL,

  sumAmount DECIMAL,
  count bigint,
  fiscalPeriod TEXT,

  type TEXT,

  project_id TEXT,

  tenantId TEXT,
  government_id TEXT,
  government_name TEXT,
  department_id  TEXT,
  department_code TEXT,
  department_name TEXT,
  departmentEntity_code TEXT,
  departmentEntity_hierarchyLevel integer,
  departmentEntity_id TEXT,
  departmentEntity_name TEXT,
  departmentEntity_ancestry_0_id TEXT,
  departmentEntity_ancestry_0_code TEXT,
  departmentEntity_ancestry_0_name TEXT,
  departmentEntity_ancestry_0_hierarchyLevel integer,
  departmentEntity_ancestry_1_id TEXT,
  departmentEntity_ancestry_1_code TEXT,
  departmentEntity_ancestry_1_name TEXT,
  departmentEntity_ancestry_1_hierarchyLevel integer,
  departmentEntity_ancestry_2_id TEXT,
  departmentEntity_ancestry_2_code TEXT,
  departmentEntity_ancestry_2_name TEXT,
  departmentEntity_ancestry_2_hierarchyLevel integer,
  departmentEntity_ancestry_3_id TEXT,
  departmentEntity_ancestry_3_code TEXT,
  departmentEntity_ancestry_3_name TEXT,
  departmentEntity_ancestry_3_hierarchyLevel integer,
  departmentEntity_ancestry_4_id TEXT,
  departmentEntity_ancestry_4_code TEXT,
  departmentEntity_ancestry_4_name TEXT,
  departmentEntity_ancestry_4_hierarchyLevel integer,
  departmentEntity_ancestry_5_id TEXT,
  departmentEntity_ancestry_5_code TEXT,
  departmentEntity_ancestry_5_name TEXT,
  departmentEntity_ancestry_5_hierarchyLevel integer,
  departmentEntity_ancestry_6_id TEXT,
  departmentEntity_ancestry_6_code TEXT,
  departmentEntity_ancestry_6_name TEXT,
  departmentEntity_ancestry_6_hierarchyLevel integer,
  departmentEntity_ancestry_7_id TEXT,
  departmentEntity_ancestry_7_code TEXT,
  departmentEntity_ancestry_7_name TEXT,
  departmentEntity_ancestry_7_hierarchyLevel integer,
  departmentEntity_ancestry_8_id TEXT,
  departmentEntity_ancestry_8_code TEXT,
  departmentEntity_ancestry_8_name TEXT,
  departmentEntity_ancestry_8_hierarchyLevel integer,
  departmentEntity_ancestry_9_id TEXT,
  departmentEntity_ancestry_9_code TEXT,
  departmentEntity_ancestry_9_name TEXT,
  departmentEntity_ancestry_9_hierarchyLevel integer,
  departmentEntity_ancestry_10_id TEXT,
  departmentEntity_ancestry_10_code TEXT,
  departmentEntity_ancestry_10_name TEXT,
  departmentEntity_ancestry_10_hierarchyLevel integer,
  expenditure_id TEXT,
  expenditure_code TEXT,
  expenditure_name TEXT,
  expenditure_type TEXT,
  project_code TEXT,
  project_name TEXT,

  coa_id TEXT,
  coa_coaCode TEXT,
  coa_majorHead TEXT,
  coa_majorHeadName TEXT,
  coa_majorHeadType TEXT,
  coa_subMajorHead TEXT,
  coa_subMajorHeadName TEXT,
  coa_minorHead TEXT,
  coa_minorHeadName TEXT,
  coa_subHead TEXT,
  coa_subHeadName TEXT,
  coa_groupHead TEXT,
  coa_groupHeadName TEXT,
  coa_objectHead TEXT,
  coa_objectHeadName TEXT,

  unique(project_id,coa_id,fiscalPeriod,type),
  PRIMARY KEY (id)
);
