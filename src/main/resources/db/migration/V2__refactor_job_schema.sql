-- Add new columns
ALTER TABLE jobs
    ADD COLUMN company_id BIGINT,
    ADD COLUMN normalized_title VARCHAR(255),
    ADD COLUMN workplace_type VARCHAR(50),
    ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    ADD COLUMN closed_at TIMESTAMP;

-- Rename existing posted_at
ALTER TABLE jobs
    RENAME COLUMN posted_at TO published_at;

-- Remove fields that are no longer part of Job
ALTER TABLE jobs
DROP COLUMN company_name,
    DROP COLUMN source,
    DROP COLUMN source_url;

-- Job -> Company relationship
ALTER TABLE jobs
    ADD CONSTRAINT fk_jobs_company
        FOREIGN KEY (company_id)
            REFERENCES company(id);

CREATE TABLE company (
       id BIGSERIAL PRIMARY KEY,

       name VARCHAR(255) NOT NULL,
       website VARCHAR(500),

       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);