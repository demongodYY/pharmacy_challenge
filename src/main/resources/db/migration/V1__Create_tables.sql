-- V1__Create_tables.sql
-- Flyway migration script for creating the initial pharma supply chain schema.
-- Target Database: PostgreSQL

-- Drugs Table: Stores information about each drug.
CREATE TABLE drugs (
                       id BIGSERIAL PRIMARY KEY,                     -- Auto-incrementing primary key
                       public_id UUID NOT NULL UNIQUE,               -- Publicly exposed unique identifier
                       name VARCHAR(100) NOT NULL,                   -- Name of the drug
                       manufacturer VARCHAR(255) NOT NULL,           -- Manufacturer of the drug
                       batch_number VARCHAR(255) NOT NULL UNIQUE,    -- Unique batch number for the drug
                       expiry_date DATE NOT NULL,                    -- Expiry date of the drug batch
                       stock INTEGER NOT NULL CHECK (stock >= 0),    -- Current stock quantity
);


-- Pharmacies Table: Stores information about each pharmacy.
CREATE TABLE pharmacies (
                            id BIGSERIAL PRIMARY KEY,
                            public_id UUID NOT NULL UNIQUE,
                            name VARCHAR(255) NOT NULL UNIQUE,            -- Name of the pharmacy
                            address VARCHAR(255)                          -- Address of the pharmacy
);


-- Pharmacy Drug Allocations Table:
-- Links pharmacies to drugs they are contracted for and their allocation limits.
CREATE TABLE pharmacy_drug_allocations (
                                           id BIGSERIAL PRIMARY KEY,
                                           pharmacy_id BIGINT NOT NULL,
                                           drug_id BIGINT NOT NULL,
                                           contracted BOOLEAN NOT NULL,                  -- Is the pharmacy contracted for this drug?
                                           allocated_amount INTEGER NOT NULL CHECK (allocated_amount >= 0), -- Max amount pharmacy can dispense

                                           CONSTRAINT uq_pharmacy_drug_allocation UNIQUE (pharmacy_id, drug_id), -- Each pharmacy-drug pair is unique
                                           CONSTRAINT fk_pda_pharmacy FOREIGN KEY (pharmacy_id) REFERENCES pharmacies (id) ON DELETE CASCADE, -- If pharmacy deleted, allocations are removed
                                           CONSTRAINT fk_pda_drug FOREIGN KEY (drug_id) REFERENCES drugs (id) ON DELETE CASCADE         -- If drug deleted, allocations are removed
);


-- Prescriptions Table: Stores prescription details.
CREATE TABLE prescriptions (
                               id BIGSERIAL PRIMARY KEY,
                               public_id UUID NOT NULL UNIQUE,
                               patient_id VARCHAR(255) NOT NULL,             -- Identifier for the patient
                               pharmacy_id BIGINT NOT NULL,                  -- Pharmacy fulfilling the prescription
                               status VARCHAR(50) NOT NULL,                  -- PENDING, FULFILLED, FAILED_VALIDATION, FAILED_FULFILLMENT
                               prescription_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- When the prescription was created
                               fulfillment_date TIMESTAMP WITHOUT TIME ZONE,   -- When the prescription was fulfilled
                               failure_reason TEXT,                          -- Reason if the prescription failed

                               CONSTRAINT fk_prescription_pharmacy FOREIGN KEY (pharmacy_id) REFERENCES pharmacies (id) ON DELETE RESTRICT -- Prevent deleting pharmacy with prescriptions
);


-- Prescription Items Table:
-- Each row represents a specific drug, its dosage, and quantity within a prescription.
CREATE TABLE prescription_items (
                                    id BIGSERIAL PRIMARY KEY,
                                    prescription_id BIGINT NOT NULL,
                                    drug_id BIGINT NOT NULL,
                                    dosage VARCHAR(255) NOT NULL,                 -- Dosage information, e.g., "1 tablet", "10mg"
                                    requested_quantity INTEGER NOT NULL CHECK (requested_quantity > 0),
                                    dispensed_quantity INTEGER CHECK (dispensed_quantity IS NULL OR dispensed_quantity >= 0), -- Quantity dispensed upon fulfillment

                                    CONSTRAINT fk_pi_prescription FOREIGN KEY (prescription_id) REFERENCES prescriptions (id) ON DELETE CASCADE, -- If prescription is deleted, its items are also deleted
                                    CONSTRAINT fk_pi_drug FOREIGN KEY (drug_id) REFERENCES drugs (id) ON DELETE RESTRICT                 -- Prevent deleting a drug if it's in any prescription item
);


-- Audit Logs Table: Logs prescription attempts (successful or failed).
CREATE TABLE audit_logs (
                            id BIGSERIAL PRIMARY KEY,
                            prescription_public_id UUID NOT NULL,         -- References Prescription.public_id (conceptual link)
                            patient_id VARCHAR(255) NOT NULL,
                            pharmacy_public_id UUID NOT NULL,             -- References Pharmacy.public_id (conceptual link)
                            drugs_requested TEXT,                         -- JSON string or similar representation of requested drugs
                            drugs_dispensed TEXT,                         -- JSON string or similar representation of dispensed drugs (if successful)
                            status VARCHAR(50) NOT NULL,                  -- SUCCESS, FAILURE
                            failure_reasons TEXT,                         -- Detailed reasons for failure
                            timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP -- When the log entry was created
);