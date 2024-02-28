ALTER TABLE eg_program_sanction
RENAME COLUMN sanctioned_amount TO net_amount;
ALTER TABLE eg_program_sanction
ADD COLUMN gross_amount DOUBLE PRECISION;

ALTER TABLE eg_program_allocation
RENAME COLUMN amount TO net_amount;
ALTER TABLE eg_program_allocation
ADD COLUMN gross_amount DOUBLE PRECISION;
