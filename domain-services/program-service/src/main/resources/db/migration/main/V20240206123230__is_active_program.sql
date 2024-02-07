ALTER TABLE eg_program ADD COLUMN is_active BOOLEAN;
ALTER TABLE eg_program_disburse ADD COLUMN disburse_parent_id character varying(64);