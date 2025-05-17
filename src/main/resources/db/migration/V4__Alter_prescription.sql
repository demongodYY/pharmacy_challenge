-- V4__Alter_prescription_items.sql
-- Alter prescription:  add version column

ALTER TABLE prescriptions
    ADD COLUMN version bigint NOT NULL DEFAULT 0;