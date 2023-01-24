CREATE TABLE department
(
    id character varying(255)  NOT NULL,
    code character varying(255),
    created_by character varying(255),
    created_time bigint,
    is_nodal boolean,
    last_modified_by character varying(255),
    last_modified_time bigint,
    name character varying(255),
    parent character varying(255),
    tenant_id character varying(255),
    CONSTRAINT department_pkey PRIMARY KEY (id)
);

CREATE TABLE expenditure
(
    id character varying(255)  NOT NULL,
    code character varying(255),
    created_by character varying(255),
    created_time bigint,
    department_id character varying(255),
    last_modified_by character varying(255),
    last_modified_time bigint,
    name character varying(255),
    tenant_id character varying(255),
    type character varying(255),
    CONSTRAINT expenditure_pkey PRIMARY KEY (id)
);

CREATE TABLE project
(
    id character varying(255)  NOT NULL,
    code character varying(255),
    created_by character varying(255),
    created_time bigint,
    expenditure_id character varying(255),
    last_modified_by character varying(255),
    last_modified_time bigint,
    name character varying(255),
    tenant_id character varying(255),
    CONSTRAINT project_pkey PRIMARY KEY (id)
);



CREATE TABLE project_department_entity_relationship
(
    project_id character varying(64) NOT NULL,
    department_entity_id character varying(64),
    status boolean,

    PRIMARY KEY (project_id, department_entity_id)
);

CREATE TABLE project_location_relationship
(
    project_id character varying(64) NOT NULL,
    location_id character varying(64),
    status boolean,

    PRIMARY KEY (project_id, location_id)
);