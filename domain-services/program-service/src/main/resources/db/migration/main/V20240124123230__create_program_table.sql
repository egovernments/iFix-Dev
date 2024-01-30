CREATE TABLE eg_program (
  id                            character varying(64),
  location_code                 character varying(64) NOT NULL,
  program_code                  character varying(64),
  name                          character varying(64),
  parent_id                     character varying(64),
  description                   character varying(256),
  client_host_url               character varying(128),
  status                        character varying(64),
  status_message                character varying(256)
  start_date                    bigint,
  end_date                      bigint,
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
  type                        character varying(64),
  parent_id                   character varying(64),
  function_code               character varying(128),
  administration_code         character varying(128),
  program_code                character varying(128),
  recipient_segment_code      character varying(128),
  economic_segment_code       character varying(128),
  source_of_fund_code         character varying(128),
  target_segment_code         character varying(128),
  additional_details          JSONB,
  created_by                  character varying(256),
  last_modified_by            character varying(256),
  created_time                bigint,
  last_modified_time          bigint,
  CONSTRAINT pk_eg_program_message_codes PRIMARY KEY (id)
);


CREATE INDEX IF NOT EXISTS index_eg_program_id ON eg_program (id);
CREATE INDEX IF NOT EXISTS index_eg_program_location_code ON eg_program (location_code);
CREATE INDEX IF NOT EXISTS index_eg_program_status ON eg_program (status);
CREATE INDEX IF NOT EXISTS index_eg_program_program_code ON eg_program (program_code);
CREATE INDEX IF NOT EXISTS index_eg_program_startDate ON eg_program (start_date);
CREATE INDEX IF NOT EXISTS index_eg_program_endDate ON eg_program (end_date);
CREATE INDEX IF NOT EXISTS index_eg_program_createdTime ON eg_program (created_time);

CREATE INDEX IF NOT EXISTS index_eg_program_message_code_id ON eg_program_message_codes (id);
CREATE INDEX IF NOT EXISTS index_eg_program_message_code_location_code ON eg_program_message_codes (location_code);
CREATE INDEX IF NOT EXISTS index_eg_program_message_code_created_time ON eg_program_message_codes (created_time);
