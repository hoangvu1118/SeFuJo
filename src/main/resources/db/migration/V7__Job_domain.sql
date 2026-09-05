CREATE TABLE platforms (
       id BIGSERIAL PRIMARY KEY,

       name VARCHAR(100) NOT NULL UNIQUE,
       website VARCHAR(500),

       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Jobs table is created in V2

CREATE TABLE job_postings (
      id BIGSERIAL PRIMARY KEY,

      job_id BIGINT NOT NULL,
      platform_id BIGINT NOT NULL,

      external_job_id VARCHAR(255),

      source_url VARCHAR(1000) NOT NULL,

      status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',

      source_published_at TIMESTAMP,

      first_seen_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      last_seen_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      closed_at TIMESTAMP,

      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

      CONSTRAINT fk_job_postings_job
          FOREIGN KEY (job_id)
              REFERENCES jobs(id)
              ON DELETE CASCADE,

      CONSTRAINT fk_job_postings_platform
          FOREIGN KEY (platform_id)
              REFERENCES platforms(id)
);

-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX idx_jobs_company_id
    ON jobs(company_id);

CREATE INDEX idx_job_postings_job_id
    ON job_postings(job_id);

CREATE INDEX idx_job_postings_platform_id
    ON job_postings(platform_id);

-- Prevent the exact same external posting from being
-- inserted repeatedly when the crawler runs again.
--
-- PostgreSQL allows multiple NULL external_job_id values,
-- so sources without an external ID are still supported.

CREATE UNIQUE INDEX uq_job_postings_platform_external_id
    ON job_postings(platform_id, external_job_id)
    WHERE external_job_id IS NOT NULL;

ALTER TABLE Jobs
DROP COLUMN published_at;