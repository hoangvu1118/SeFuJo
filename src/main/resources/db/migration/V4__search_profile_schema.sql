CREATE TABLE search_profiles (
     id BIGSERIAL PRIMARY KEY,

     user_id BIGINT NOT NULL,
     name VARCHAR(100) NOT NULL,

     created_at TIMESTAMP NOT NULL,
     updated_at TIMESTAMP NOT NULL,

     CONSTRAINT fk_search_profile_user
         FOREIGN KEY (user_id)
             REFERENCES users(id)
);

CREATE TABLE search_profile_job_titles (
   search_profile_id BIGINT NOT NULL,
   job_title VARCHAR(150) NOT NULL,

   FOREIGN KEY (search_profile_id)
       REFERENCES search_profiles(id)
       ON DELETE CASCADE
);

CREATE TABLE search_profile_locations (
  search_profile_id BIGINT NOT NULL,
  location VARCHAR(150) NOT NULL,

  FOREIGN KEY (search_profile_id)
      REFERENCES search_profiles(id)
      ON DELETE CASCADE
);

CREATE TABLE search_profile_skills (
    search_profile_id BIGINT NOT NULL,
    skill VARCHAR(100) NOT NULL,

    FOREIGN KEY (search_profile_id)
       REFERENCES search_profiles(id)
       ON DELETE CASCADE
);

CREATE TABLE search_profile_employment_types (
     search_profile_id BIGINT NOT NULL,
     employment_type VARCHAR(50) NOT NULL,

     FOREIGN KEY (search_profile_id)
         REFERENCES search_profiles(id)
         ON DELETE CASCADE
);

CREATE TABLE search_profile_workplace_types (
    search_profile_id BIGINT NOT NULL,
    workplace_type VARCHAR(50) NOT NULL,

    FOREIGN KEY (search_profile_id)
        REFERENCES search_profiles(id)
        ON DELETE CASCADE
    );