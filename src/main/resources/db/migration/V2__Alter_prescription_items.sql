-- V2__Alter_prescription_items.sql
-- Alter prescription_items: dosage to integer, remove dispensed_quantity/requested_quantity

ALTER TABLE prescription_items
ALTER COLUMN dosage TYPE INTEGER USING dosage::integer,
    ALTER COLUMN dosage SET NOT NULL,
    ADD CONSTRAINT chk_prescription_items_dosage_positive CHECK (dosage > 0);

ALTER TABLE prescription_items
DROP COLUMN requested_quantity,
    DROP COLUMN dispensed_quantity;