ALTER TABLE eg_program RENAME COLUMN disburse_parent_id TO parent_id;
ALTER TABLE eg_program_message_codes DROP COLUMN additional_details
ALTER TABLE eg_program ADD COLUMN additional_details JSONB;
ALTER TABLE eg_program_sanction ADD COLUMN additional_details JSONB;
ALTER TABLE eg_program_allocation ADD COLUMN additional_details JSONB;
ALTER TABLE eg_program_disburse ADD COLUMN additional_details JSONB;
ALTER TABLE eg_program_transaction_logs ADD COLUMN additional_details JSONB;
