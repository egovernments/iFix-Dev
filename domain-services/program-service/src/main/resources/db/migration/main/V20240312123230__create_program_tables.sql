CREATE TABLE eg_program (
  id                            character varying(64),
  location_code                 character varying(64) NOT NULL,
  program_code                  character varying(64),
  name                          character varying(64),
  parent_id                     character varying(64),
  description                   character varying(256),
  status                        character varying(64),
  status_message                character varying,
  start_date                    bigint,
  end_date                      bigint,
  additional_details            JSONB,
  created_by                    character varying(256),
  last_modified_by              character varying(256),
  created_time                  bigint,
  last_modified_time            bigint,
  CONSTRAINT uk_eg_program      UNIQUE (program_code),
  CONSTRAINT pk_eg_program      PRIMARY KEY (id)
);

CREATE TABLE eg_program_message_codes (
  id                          character varying(64),
  location_code               character varying(64) NOT NULL,
  type                        character varying(64) NOT NULL,
  reference_id                character varying(64),
  function_code               character varying(128),
  administration_code         character varying(128),
  program_code                character varying(128),
  recipient_segment_code      character varying(128),
  economic_segment_code       character varying(128),
  source_of_fund_code         character varying(128),
  target_segment_code         character varying(128),
  currency_code               character varying(128),
  locale_code                 character varying(128),
  created_by                  character varying(256),
  last_modified_by            character varying(256),
  created_time                bigint,
  last_modified_time          bigint,
  CONSTRAINT pk_eg_program_message_codes PRIMARY KEY (id)
);


CREATE TABLE eg_program_sanction (
  id                            character varying(64),
  location_code                 character varying(64) NOT NULL,
  program_code                  character varying(64),
  gross_amount                  DOUBLE PRECISION,
  net_amount                    DOUBLE PRECISION,
  allocated_amount              DOUBLE PRECISION,
  available_amount              DOUBLE PRECISION,
  status                        character varying(64),
  status_message                character varying,
  additional_details            JSONB,
  created_by                    character varying(256),
  last_modified_by              character varying(256),
  created_time                  bigint,
  last_modified_time            bigint,
  CONSTRAINT pk_eg_program_sanction      PRIMARY KEY (id),
  CONSTRAINT fk_eg_program_sanction FOREIGN KEY (program_code) REFERENCES eg_program (program_code)
);

CREATE TABLE eg_program_allocation (
  id                            character varying(64),
  location_code                 character varying(64) NOT NULL,
  program_code                  character varying(64),
  sanction_id                   character varying(64),
  gross_amount                  DOUBLE PRECISION,
  net_amount                    DOUBLE PRECISION,
  status                        character varying(64),
  status_message                character varying,
  allocation_type               character varying(64),
  additional_details            JSONB,
  created_by                    character varying(256),
  last_modified_by              character varying(256),
  created_time                  bigint,
  last_modified_time            bigint,
  CONSTRAINT pk_eg_program_allocation      PRIMARY KEY (id),
  CONSTRAINT fk_eg_program_allocation FOREIGN KEY (program_code) REFERENCES eg_program (program_code)
);

CREATE TABLE eg_program_disburse (
  id                            character varying(64),
  location_code                 character varying(64) NOT NULL,
  program_code                  character varying(64),
  parent_id                     character varying(64),
  target_id                     character varying(64),
  transaction_id                character varying(64),
  sanction_id                   character varying(64),
  account_code                  character varying(128),
  individual                    JSONB,
  net_amount                    DOUBLE PRECISION,
  gross_amount                  DOUBLE PRECISION,
  status                        character varying(64),
  status_message                character varying,
  additional_details            JSONB,
  created_by                    character varying(256),
  last_modified_by              character varying(256),
  created_time                  bigint,
  last_modified_time            bigint,
  CONSTRAINT pk_eg_program_disburse      PRIMARY KEY (id),
  CONSTRAINT fk_eg_program_disburse FOREIGN KEY (program_code) REFERENCES eg_program (program_code)
);


CREATE TABLE eg_program_transaction_logs (
  id                            character varying(64),
  location_code                 character varying(64) NOT NULL,
  program_code                  character varying(64),
  sanction_id                   character varying(64),
  disburse_id                   character varying(64),
  type                          character varying(64),
  amount                        DOUBLE PRECISION,
  additional_details            JSONB,
  created_by                    character varying(256),
  created_time                  bigint,
  CONSTRAINT pk_eg_program_transaction_logs      PRIMARY KEY (id),
  CONSTRAINT fk_eg_program_transaction_logs FOREIGN KEY (program_code) REFERENCES eg_program (program_code)
);


CREATE INDEX IF NOT EXISTS index_eg_program_id ON eg_program (id);
CREATE INDEX IF NOT EXISTS index_eg_program_location_code ON eg_program (location_code);
CREATE INDEX IF NOT EXISTS index_eg_program_parent_id ON eg_program (parent_id);
CREATE INDEX IF NOT EXISTS index_eg_program_name ON eg_program (name);
CREATE INDEX IF NOT EXISTS index_eg_program_program_code ON eg_program (program_code);
CREATE INDEX IF NOT EXISTS index_eg_program_status ON eg_program (status);

CREATE INDEX IF NOT EXISTS index_eg_program_message_code_id ON eg_program_message_codes (id);
CREATE INDEX IF NOT EXISTS index_eg_program_message_code_location_code ON eg_program_message_codes (location_code);

CREATE INDEX IF NOT EXISTS index_eg_program_sanction_id ON eg_program_sanction (id);
CREATE INDEX IF NOT EXISTS index_eg_program_sanction_location_code ON eg_program_sanction (location_code);
CREATE INDEX IF NOT EXISTS index_eg_program_sanction_program_code ON eg_program_sanction (program_code);

CREATE INDEX IF NOT EXISTS index_eg_program_allocation_id ON eg_program_allocation (id);
CREATE INDEX IF NOT EXISTS index_eg_program_allocation_location_code ON eg_program_allocation (location_code);
CREATE INDEX IF NOT EXISTS index_eg_program_allocation_program_code ON eg_program_allocation (program_code);
CREATE INDEX IF NOT EXISTS index_eg_program_allocation_sanction_id ON eg_program_allocation (sanction_id);

CREATE INDEX IF NOT EXISTS index_eg_program_disburse_id ON eg_program_disburse (id);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_location_code ON eg_program_disburse (location_code);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_program_code ON eg_program_disburse (program_code);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_target_id ON eg_program_disburse (target_id);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_transaction_id ON eg_program_disburse (transaction_id);