CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,

                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,

                       first_name VARCHAR(100),
                       last_name VARCHAR(100),

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE jobs (
                      id BIGSERIAL PRIMARY KEY,

                      title VARCHAR(255) NOT NULL,
                      company_name VARCHAR(255) NOT NULL,

                      location VARCHAR(255),

                      employment_type VARCHAR(50),
                      experience_level VARCHAR(50),

                      description TEXT,

                      source VARCHAR(100),
                      source_url TEXT,

                      posted_at TIMESTAMP,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);