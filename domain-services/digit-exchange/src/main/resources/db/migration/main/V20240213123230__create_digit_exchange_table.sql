CREATE TABLE eg_digit_exchange_servers (
  id varchar(64) PRIMARY KEY,
  name varchar(256) NOT NULL,
  description varchar(256) NOT NULL,
  host_url varchar(256) NOT NULL,
  public_key varchar NOT NULL,
  is_active boolean,
  created_time bigint,
  created_by varchar(64),
  last_modified_time bigint,
  last_modified_by varchar(64)
);
