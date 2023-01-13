CREATE TABLE IF NOT EXISTS public.department_entity
(
    id character varying(255) NOT NULL,
    code character varying(64),
    created_by character varying(255),
    created_time bigint,
    department_id character varying(64),
    hierarchy_level integer,
    last_modified_by character varying(255),
    last_modified_time bigint,
    name character varying(255),
    tenant_id character varying(64),
    CONSTRAINT department_entity_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.department_hierarchy_level
(
    id character varying(255) NOT NULL,
    created_by character varying(255),
    created_time bigint,
    department_id character varying(64),
    label character varying(255),
    last_modified_by character varying(255),
    last_modified_time bigint,
    level integer,
    parent character varying(64),
    tenant_id character varying(64),
    CONSTRAINT department_hierarchy_level_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.department_entity_relationship
(
    parent_id character varying(64) NOT NULL,
    child_id character varying(64),
    status boolean,

    PRIMARY KEY (parent_id, child_id)
);