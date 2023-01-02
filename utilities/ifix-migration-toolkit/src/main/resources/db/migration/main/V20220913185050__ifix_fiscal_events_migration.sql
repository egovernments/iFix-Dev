CREATE TABLE ifix_fiscal_events_migration (

  id CHARACTER VARYING (128) NOT NULL,
  tenantid CHARACTER VARYING (128) NOT NULL,
  pagenumber bigint NOT NULL,
  batchsize bigint NOT NULL,
  totalnumberofrecordsmigrated bigint NOT NULL,
  createdtime bigint NOT NULL,

  CONSTRAINT pk_ifix_fiscal_events_migration_id PRIMARY KEY(id)
);

CREATE TABLE department_entity_migration (

  id CHARACTER VARYING (128) NOT NULL,
  tenantid CHARACTER VARYING (128) NOT NULL,
  pagenumber bigint NOT NULL,
  batchsize bigint NOT NULL,
  totalnumberofrecordsmigrated bigint NOT NULL,
  service_type character varying(255),
  createdtime bigint NOT NULL,

  CONSTRAINT pk_ifix_dept_entity_migration_id PRIMARY KEY(id)
);