DROP TABLE IF EXISTS eg_ifix_chartofaccount;

CREATE TABLE IF NOT EXISTS eg_ifix_chartofaccount
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    tenantid character varying COLLATE pg_catalog."default" NOT NULL,
    coacode character varying COLLATE pg_catalog."default",
    majorhead character varying COLLATE pg_catalog."default",
    majorheadname character varying COLLATE pg_catalog."default",
    submajorhead character varying COLLATE pg_catalog."default",
    submajorheadname character varying COLLATE pg_catalog."default",
    minorhead character varying COLLATE pg_catalog."default",
    minorheadname character varying COLLATE pg_catalog."default",
    subhead character varying COLLATE pg_catalog."default",
    subheadname character varying COLLATE pg_catalog."default",
    grouphead character varying COLLATE pg_catalog."default",
    groupheadname character varying COLLATE pg_catalog."default",
    objecthead character varying COLLATE pg_catalog."default",
    objectheadname character varying COLLATE pg_catalog."default",
    attributes jsonb,
    createdtime bigint,
    createdby character varying COLLATE pg_catalog."default",
    lastmodifiedtime bigint,
    lastmodifiedby character varying COLLATE pg_catalog."default",
    CONSTRAINT pk_ifix_chartofaccount PRIMARY KEY (id, tenantid),
    CONSTRAINT eg_ifix_chartofaccount_coacode_key UNIQUE (coacode)
);
