CREATE TABLE eg_program_sanction (
  id                            character varying(64),
  location_code                 character varying(64) NOT NULL,
  program_code                  character varying(64),
  sanctioned_amount             DOUBLE PRECISION,
  allocated_amount              DOUBLE PRECISION,
  available_amount              DOUBLE PRECISION,
  status                        character varying(64),
  status_message                character varying(256),
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
  amount                        DOUBLE PRECISION,
  status                        character varying(64),
  status_message                character varying(256),
  type                          character varying(64),
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
  target_id                     character varying(64),
  sanction_id                   character varying(64),
  account_code                  character varying(64),
  individual                    JSONB,
  net_amount                    DOUBLE PRECISION,
  gross_amount                  DOUBLE PRECISION,
  status                        character varying(64),
  status_message                character varying(256),
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
  created_by                    character varying(256),
  created_time                  bigint,
  CONSTRAINT pk_eg_program_transaction_logs      PRIMARY KEY (id),
  CONSTRAINT fk_eg_program_transaction_logs FOREIGN KEY (program_code) REFERENCES eg_program (program_code)

);



CREATE INDEX IF NOT EXISTS index_eg_program_sanction_id ON eg_program_sanction (id);
CREATE INDEX IF NOT EXISTS index_eg_program_sanction_location_code ON eg_program_sanction (location_code);
CREATE INDEX IF NOT EXISTS index_eg_program_sanction_program_code ON eg_program_sanction (program_code);
CREATE INDEX IF NOT EXISTS index_eg_program_sanction_status ON eg_program_sanction (status);
CREATE INDEX IF NOT EXISTS index_eg_program_sanction_createdTime ON eg_program_sanction (created_time);

CREATE INDEX IF NOT EXISTS index_eg_program_allocation_id ON eg_program_allocation (id);
CREATE INDEX IF NOT EXISTS index_eg_program_allocation_location_code ON eg_program_allocation (location_code);
CREATE INDEX IF NOT EXISTS index_eg_program_allocation_program_code ON eg_program_allocation (program_code);
CREATE INDEX IF NOT EXISTS index_eg_program_allocation_status ON eg_program_allocation (status);
CREATE INDEX IF NOT EXISTS index_eg_program_allocation_type ON eg_program_allocation (type);
CREATE INDEX IF NOT EXISTS index_eg_program_allocation_createdTime ON eg_program_allocation (created_time);

CREATE INDEX IF NOT EXISTS index_eg_program_disburse_id ON eg_program_disburse (id);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_location_code ON eg_program_disburse (location_code);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_program_code ON eg_program_disburse (program_code);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_target_id ON eg_program_disburse (target_id);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_sanction_id ON eg_program_disburse (sanction_id);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_status ON eg_program_disburse (status);
CREATE INDEX IF NOT EXISTS index_eg_program_disburse_createdTime ON eg_program_disburse (created_time);

