-- V2__Alter_prescription_items.sql
-- Alter prescription:  remove failure_reason

ALTER TABLE prescriptions
DROP COLUMN failure_reason;