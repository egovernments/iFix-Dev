CREATE TABLE IF NOT EXISTS public.department_entity
(
    id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    children character varying(5000) COLLATE pg_catalog."default",
    code character varying(64) COLLATE pg_catalog."default",
    created_by character varying(255) COLLATE pg_catalog."default",
    created_time bigint,
    department_id character varying(64) COLLATE pg_catalog."default",
    hierarchy_level integer,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_time bigint,
    name character varying(255) COLLATE pg_catalog."default",
    tenant_id character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT department_entity_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.department_hierarchy_level
(
    id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_time bigint,
    department_id character varying(64) COLLATE pg_catalog."default",
    label character varying(255) COLLATE pg_catalog."default",
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_time bigint,
    level integer,
    parent character varying(64) COLLATE pg_catalog."default",
    tenant_id character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT department_hierarchy_level_pkey PRIMARY KEY (id)
);