CREATE TABLE department
(
    id character varying(255)  NOT NULL,
    tenant_id character varying(255),
    code character varying(255),
    name character varying(255),
    is_nodal boolean,
    parent character varying(255),

    created_by character varying(255),
    created_time bigint,
    last_modified_by character varying(255),
    last_modified_time bigint,

    CONSTRAINT department_pkey PRIMARY KEY (id)
);

CREATE TABLE expenditure
(
    id character varying(255)  NOT NULL,
    tenant_id character varying(255),
    code character varying(255),
    name character varying(255),
    type character varying(255),
    department_id character varying(255),

    created_by character varying(255),
    created_time bigint,
    last_modified_by character varying(255),
    last_modified_time bigint,

    CONSTRAINT expenditure_pkey PRIMARY KEY (id)
);

CREATE TABLE project
(
    id character varying(255)  NOT NULL,
    tenant_id character varying(255),
    code character varying(255),
    name character varying(255),
    expenditure_id character varying(255),

    created_by character varying(255),
    created_time bigint,
    last_modified_by character varying(255),
    last_modified_time bigint,

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