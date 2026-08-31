# Job Application Booster — Implementation Roadmap

## 1. Project Goal

Build an application that helps users speed up the job-search and application process.

The intended user flow is:

1. User creates an account.
2. User defines one or more job search preferences.
3. The system continuously ingests job postings from supported sources.
4. The system normalizes and deduplicates those postings into canonical jobs.
5. The system matches jobs against each user's search profile.
6. Suitable jobs are surfaced as recommendations.
7. The user's resume is tailored to a selected job description.
8. The user reviews the tailored resume.
9. The user applies manually or through supported automated application flows.
10. The system tracks every application and its current status.

---

# 2. Core Domain Model

## User-side entities

### User

Represents the authenticated account.

Typical fields:

- `id`
- `email`
- `password_hash`
- `name`
- `created_at`
- `updated_at`

A `User` answers:

> Who is using the application?

---

### SearchProfile

Represents what kinds of jobs the user wants.

A user may eventually have multiple profiles such as:

- Backend Internship Vietnam
- Java Working Student Germany
- Graduate Software Engineer Singapore

Typical fields:

- `id`
- `user_id`
- `name`
- `target_titles`
- `preferred_locations`
- `employment_types`
- `experience_levels`
- `minimum_salary`
- `remote_allowed`
- `required_keywords`
- `excluded_keywords`
- `active`
- `created_at`
- `updated_at`

A `SearchProfile` answers:

> What kind of jobs is this user looking for?

For the first MVP, a user can be limited to one active profile.

---

### Resume

Represents a resume owned by the user.

Typical fields:

- `id`
- `user_id`
- `name`
- `is_default`
- `created_at`
- `updated_at`

---

### ResumeVersion

Represents one version of a resume.

Do not separate `ResumeOG` and `ResumeModified` into different entity types.

Instead use:

```text
Resume
 ├── ResumeVersion BASE
 ├── ResumeVersion TAILORED FOR BOSCH
 ├── ResumeVersion TAILORED FOR NAB
 └── ResumeVersion USER_EDITED
```

Typical fields:

- `id`
- `resume_id`
- `parent_version_id`
- `job_id`
- `type`
- `content`
- `file_url`
- `llm_model`
- `prompt_version`
- `created_at`

Possible types:

- `BASE`
- `TAILORED`
- `USER_EDITED`

---

## Job-side entities

### Company

Represents the hiring company.

Examples:

- Bosch
- NAB
- Grab
- Shopee

Typical fields:

- `id`
- `name`
- `website`
- `created_at`
- `updated_at`

Relationship:

```text
Company 1 ----- * Job
```

One company can have many jobs.

---

### Job

Represents the canonical vacancy in the application's database.

Example:

```text
Backend Engineer Intern
Bosch
Ho Chi Minh City
```

Typical fields:

- `id`
- `company_id`
- `title`
- `normalized_title`
- `location`
- `employment_type`
- `workplace_type`
- `description`
- `status`
- `published_at`
- `closed_at`
- `created_at`
- `updated_at`

A `Job` answers:

> What real-world vacancy do we believe exists?

---

### Platform

Represents a source/platform from which a posting was discovered.

Examples:

- LinkedIn
- Indeed
- Bosch Careers
- Lever
- Ashby

Typical fields:

- `id`
- `name`
- `type`
- `base_url`

---

### JobPosting

Represents one source-specific advertisement for a canonical `Job`.

Example:

```text
Job #101
Backend Engineer Intern — Bosch

├── JobPosting from Bosch Careers
├── JobPosting from LinkedIn
└── JobPosting from Indeed
```

Typical fields:

- `id`
- `job_id`
- `platform_id`
- `external_job_id`
- `source_url`
- `apply_url`
- `raw_title`
- `raw_description`
- `raw_payload`
- `content_hash`
- `first_seen_at`
- `last_seen_at`
- `last_crawled_at`
- `source_status`
- `created_at`
- `updated_at`

A `JobPosting` answers:

> Where did we discover this job?

This separation allows the system to deduplicate the same vacancy appearing on multiple sources.

---

## Recommendation-side entity

### JobRecommendation

Represents the relationship:

> This system believes this job is relevant to this user/search profile.

Typical fields:

- `id`
- `user_id`
- `job_id`
- `search_profile_id`
- `match_score`
- `match_reasons`
- `status`
- `created_at`
- `updated_at`

Possible statuses:

- `NEW`
- `VIEWED`
- `SAVED`
- `DISMISSED`
- `READY_TO_APPLY`

Example:

```text
User #1 + Job #42

match_score = 91
status = SAVED
```

This entity allows the application to remember that a user already saw, saved, or dismissed a recommendation.

---

## Application-side entities

### Application

Represents a real application by one user to one job.

This is not merely a join table.

Typical fields:

- `id`
- `user_id`
- `job_id`
- `job_posting_id`
- `resume_version_id`
- `status`
- `application_method`
- `external_application_id`
- `submitted_at`
- `failure_reason`
- `created_at`
- `updated_at`

Possible methods:

- `MANUAL`
- `REDIRECT`
- `AUTOMATED`

Possible statuses:

- `DRAFT`
- `TAILORING`
- `READY_FOR_REVIEW`
- `APPROVED`
- `SUBMITTING`
- `SUBMITTED`
- `INTERVIEW`
- `OFFER`
- `REJECTED`
- `FAILED`
- `WITHDRAWN`

---

### ApplicationStatusHistory

Stores the lifecycle of an application.

Typical fields:

- `id`
- `application_id`
- `from_status`
- `to_status`
- `reason`
- `created_at`

Example:

```text
SUBMITTED -> INTERVIEW
INTERVIEW -> OFFER
```

---

# 3. Overall Database Relationship

```text
                         USER SIDE

                         ┌──────────┐
                         │   User   │
                         └────┬─────┘
                              │
             ┌────────────────┼─────────────────┐
             │                │                 │
             ▼                ▼                 │
      ┌─────────────┐    ┌─────────┐            │
      │SearchProfile│    │ Resume  │            │
      └─────────────┘    └────┬────┘            │
                              │                 │
                              ▼                 │
                       ┌──────────────┐          │
                       │ResumeVersion │          │
                       └──────────────┘          │
                                                │
                                                ▼
                                      ┌───────────────────┐
                                      │JobRecommendation  │
                                      └─────────┬─────────┘
                                                │
                                                ▼

                         JOB SIDE

                                          ┌───────────┐
                                          │    Job    │
                                          └────┬──────┘
                                               │
                           ┌───────────────────┼───────────────┐
                           │                                   │
                           ▼                                   ▼
                      ┌─────────┐                        ┌───────────┐
                      │ Company │                        │JobPosting │
                      └─────────┘                        └─────┬─────┘
                                                            │
                                                            ▼
                                                       ┌─────────┐
                                                       │Platform │
                                                       └─────────┘
```

Application flow:

```text
User
 │
 ▼
Application
 │
 ├──── Job
 │
 ├──── JobPosting
 │
 ├──── ResumeVersion
 │
 └──── ApplicationStatusHistory
```

---

# 4. SearchProfile Storage

A relational database can store search preferences well.

Recommended starting point:

- PostgreSQL
- relational `SearchProfile`
- `@ElementCollection` for simple multi-value preferences

Conceptually:

```java
@Entity
public class SearchProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String name;

    private Integer minimumSalary;

    private boolean remoteAllowed;

    private boolean active;

    @ElementCollection
    private Set<String> targetTitles;

    @ElementCollection
    private Set<String> preferredLocations;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<EmploymentType> employmentTypes;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<ExperienceLevel> experienceLevels;
}
```

Hibernate can map these collections into child tables such as:

```text
search_profiles

id | user_id | name
10 | 1       | Backend Search
```

and:

```text
search_profile_target_titles

search_profile_id | title
10                | Backend Developer
10                | Java Developer
```

Avoid comma-separated values such as:

```text
"Backend Developer,Java Developer,Software Engineer"
```

---

# 5. Recommended Technology Foundation

Initial backend stack:

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker Compose
- JWT authentication

Add later only when there is a real use case:

- Kafka
- Redis
- object storage such as MinIO/S3
- LLM provider
- Playwright
- asynchronous workers

---

# 6. Recommended Code Organization

Prefer feature/domain-based packages instead of global technical folders.

Recommended:

```text
src/main/java/...

auth/
user/
searchprofile/
job/
resume/
application/
common/
```

Example:

```text
job/
 ├── Job.java
 ├── JobController.java
 ├── JobService.java
 ├── JobRepository.java
 ├── JobPosting.java
 ├── Company.java
 ├── Platform.java
 └── dto/
```

Avoid a large global structure like:

```text
controller/
service/
repository/
entity/
```

because it becomes hard to navigate as the project grows.

---

# 7. Implementation Roadmap

## Milestone 0 — Project Foundation

Goal:

> Spring Boot can start, connect to PostgreSQL, run migrations, and expose a health endpoint.

Tasks:

- [ ] Create Spring Boot project
- [ ] Add PostgreSQL
- [ ] Add Docker Compose
- [ ] Add Flyway
- [ ] Configure Spring Data JPA
- [ ] Add basic exception handling
- [ ] Create `GET /health`

Definition of done:

```http
GET /health
```

returns:

```text
200 OK
```

and PostgreSQL schema is created automatically via Flyway.

---

# 8. Milestone 1 — Authentication

Use JWT for the initial backend.

Keep authentication intentionally small.

Endpoints:

```http
POST /auth/register
POST /auth/login
GET  /me
```

Tasks:

- [ ] Create `User`
- [ ] Register endpoint
- [ ] BCrypt password hashing
- [ ] Login endpoint
- [ ] JWT generation
- [ ] JWT validation
- [ ] JWT authentication filter
- [ ] Spring Security configuration
- [ ] Protected `GET /me`
- [ ] Authorization tests

Initial flow:

```text
Register
   ↓
User persisted
   ↓
Login
   ↓
JWT
   ↓
Authorization: Bearer <token>
   ↓
Spring Security
   ↓
Authenticated User
```

Do not implement initially:

- refresh tokens
- Google OAuth
- password reset
- email verification
- social login

Add these only when needed.

Definition of done:

> A new user can register, log in, receive a JWT, and successfully call a protected endpoint.

---

# 9. Milestone 2 — Search Profile

Goal:

> An authenticated user can define what kinds of jobs they want.

Endpoints:

```http
POST   /search-profiles
GET    /search-profiles
GET    /search-profiles/{id}
PUT    /search-profiles/{id}
DELETE /search-profiles/{id}
```

Example payload:

```json
{
  "name": "Backend Internship",
  "targetTitles": [
    "Backend Developer",
    "Java Developer"
  ],
  "preferredLocations": [
    "Ho Chi Minh City",
    "Remote"
  ],
  "employmentTypes": [
    "INTERNSHIP"
  ],
  "experienceLevels": [
    "ENTRY_LEVEL"
  ]
}
```

Tasks:

- [ ] Create `SearchProfile`
- [ ] Create collection mappings
- [ ] Create DTOs
- [ ] Create CRUD endpoints
- [ ] Verify ownership
- [ ] Ensure User A cannot access User B's profile
- [ ] Add tests

Definition of done:

> An authenticated user can create, update, view, and delete their job-search preferences securely.

---

# 10. Milestone 3 — Job Domain

Goal:

> The application can store and expose job information before any crawler exists.

Entities:

- `Company`
- `Platform`
- `Job`
- `JobPosting`

Tasks:

- [ ] Create entities
- [ ] Add repositories
- [ ] Create Flyway migrations
- [ ] Add fake/test job data
- [ ] Implement `GET /jobs`
- [ ] Implement `GET /jobs/{id}`
- [ ] Add filtering

Suggested filters:

```http
GET /jobs?title=backend
GET /jobs?location=hcm
GET /jobs?employmentType=INTERNSHIP
```

Important:

Do not start crawling yet.

Create test data manually first so the domain can be verified independently.

Definition of done:

> The backend can correctly represent companies, jobs, source postings, and platforms.

---

# 11. Milestone 4 — Job Ingestion

Goal:

> One external job source can be fetched and converted into the internal job model.

Start with only one predictable source.

Do not start with multiple sources simultaneously.

Architecture:

```text
External source
      ↓
Source Adapter
      ↓
RawJobPosting
      ↓
JobIngestionService
      ↓
Normalizer
      ↓
Deduplication
      ↓
Company + Job + JobPosting
      ↓
PostgreSQL
```

Recommended abstraction:

```java
public interface JobSource {

    List<RawJobPosting> fetchJobs();

}
```

Example raw DTO:

```java
public record RawJobPosting(
    String externalId,
    String company,
    String title,
    String description,
    String location,
    String url
) {}
```

Important separation:

Avoid:

```text
Crawler -> Repository
```

Prefer:

```text
Crawler
   ↓
RawJobPosting
   ↓
JobIngestionService
   ↓
Domain entities
   ↓
Repositories
```

Tasks:

- [ ] `JobSource` abstraction
- [ ] One source adapter
- [ ] `RawJobPosting`
- [ ] Normalization logic
- [ ] Duplicate detection
- [ ] `first_seen_at`
- [ ] `last_seen_at`
- [ ] `content_hash`
- [ ] scheduled ingestion
- [ ] crawler failure handling

Do not physically delete jobs simply because one crawl missed them.

---

# 12. Milestone 5 — Recommendation Engine

Goal:

> Search profiles and jobs become connected.

Start with deterministic scoring.

Do not start with embeddings or LLM matching.

Example score:

```text
Title match             +40
Location match          +20
Employment type match   +20
Keyword overlap         +20
                       ----
                        100
```

Tasks:

- [ ] Create `JobRecommendation`
- [ ] Build scoring service
- [ ] Generate recommendations
- [ ] Store `match_score`
- [ ] Store `match_reasons`
- [ ] Support statuses
- [ ] Implement recommendation API

Endpoints:

```http
GET /job-recommendations
PATCH /job-recommendations/{id}
```

Possible user actions:

- save
- dismiss
- mark viewed

Definition of done:

> An authenticated user can see a ranked list of jobs based on their SearchProfile.

---

# 13. Milestone 6 — Resume Management

Goal:

> Users can upload and manage resumes before any LLM modification happens.

Entities:

- `Resume`
- `ResumeVersion`

Tasks:

- [ ] Resume upload
- [ ] Store metadata
- [ ] Store original file
- [ ] Extract text
- [ ] Create base `ResumeVersion`
- [ ] Retrieve resume
- [ ] Delete/archive resume

Recommended storage model:

```text
PostgreSQL:
- metadata
- parsed content
- storage key

Object storage:
- actual PDF/DOCX file
```

For the first local implementation, local filesystem storage is acceptable.

Later replace with:

- MinIO
- S3
- another object store

---

# 14. Milestone 7 — AI Resume Tailoring

Goal:

> Generate a job-specific resume from an existing resume without inventing experience.

Flow:

```text
Job
 +
Base ResumeVersion
      ↓
ResumeTailoringService
      ↓
LLM Provider
      ↓
Tailored ResumeVersion
```

Recommended abstraction:

```java
public interface ResumeTailoringProvider {

    TailoredResume tailor(...);

}
```

Then:

```text
OpenAiResumeTailoringProvider
```

can be one implementation.

Rules:

Allowed:

- rephrase experience
- reorder bullets
- emphasize relevant experience
- use relevant JD terminology
- remove irrelevant content

Not allowed:

- invent skills
- invent employment
- fabricate years of experience
- add unsupported achievements

Endpoint:

```http
POST /resume-tailorings
```

Example:

```json
{
  "jobId": 42,
  "resumeId": 7
}
```

Result:

```text
ResumeVersion TYPE = TAILORED
```

---

# 15. Milestone 8 — Application Tracking

Goal:

> Track applications before trying to automate submission.

Start manually.

Endpoints:

```http
POST  /applications
GET   /applications
GET   /applications/{id}
PATCH /applications/{id}/status
```

Example dashboard:

```text
Company     Position         Status
--------------------------------------
Bosch       Backend Intern   SUBMITTED
NAB         Java Intern      INTERVIEW
Grab        SWE              REJECTED
```

Tasks:

- [ ] Create `Application`
- [ ] Create `ApplicationStatusHistory`
- [ ] Support statuses
- [ ] Record resume version
- [ ] Record job/job posting
- [ ] Build dashboard data
- [ ] Add export endpoint later

The dashboard replaces the earlier `ExcelSheet` entity idea.

Excel/CSV is an output format, not a domain entity.

---

# 16. Milestone 9 — Application Automation

Goal:

> Automate application submission only for supported sources.

Recommended abstraction:

```java
public interface ApplicationAdapter {

    ApplicationResult submit(
        JobPosting posting,
        CandidateProfile candidate,
        ResumeVersion resume
    );

}
```

Possible implementations:

```text
SupportedATSAdapter
ManualRedirectAdapter
```

Do not try to build a universal Playwright bot immediately.

Start with:

- one supported ATS
- one predictable workflow

Possible result:

```text
SUBMITTED
FAILED
REQUIRES_USER_ACTION
```

---

# 17. Milestone 10 — Kafka / Event-Driven Processing

Kafka should be added only once there are natural asynchronous boundaries.

Potential events:

```text
job.discovered
job.created
job.updated
job.matched
resume.tailoring.requested
resume.tailored
application.submission.requested
application.submitted
application.failed
```

Before Kafka:

```text
Crawler
  ↓
JobIngestionService
  ↓
RecommendationService
```

After Kafka:

```text
Crawler
   ↓
job.discovered
   ↓
Kafka
   ├── Job Processor
   ├── Recommendation Processor
   └── Analytics
```

Tasks:

- [ ] Kafka local Docker setup
- [ ] event contracts
- [ ] producer
- [ ] consumers
- [ ] retry strategy
- [ ] dead-letter handling
- [ ] idempotency

Use Kafka because a real async problem exists, not merely because the project should contain Kafka.

---

# 18. API Direction

Recommended initial API surface:

## Authentication

```http
POST /auth/register
POST /auth/login
GET  /me
```

## Search Profiles

```http
GET    /search-profiles
POST   /search-profiles
GET    /search-profiles/{id}
PUT    /search-profiles/{id}
DELETE /search-profiles/{id}
```

## Jobs

```http
GET /jobs
GET /jobs/{id}
```

## Recommendations

```http
GET   /job-recommendations
PATCH /job-recommendations/{id}
```

## Resumes

```http
GET  /resumes
POST /resumes
GET  /resumes/{id}
```

## Resume Tailoring

```http
POST /resume-tailorings
GET  /resume-tailorings/{id}
```

## Applications

```http
GET   /applications
POST  /applications
GET   /applications/{id}
PATCH /applications/{id}/status
```

## Dashboard

```http
GET /dashboard
```

## Export

```http
GET /applications/export?format=xlsx
```

---

# 19. Development Principle

Do not define work as:

> Implement authentication.

Prefer vertical definitions of done:

> A new user can register, log in, receive a JWT, and create a SearchProfile that another user cannot access.

Similarly:

> A user with a SearchProfile can retrieve ranked recommendations from manually seeded jobs.

This keeps each milestone small, testable, and useful.

---

# 20. Immediate Implementation Order

The recommended next order is:

```text
1. Project setup
      ↓
2. JWT authentication
      ↓
3. SearchProfile
      ↓
4. Job domain with manually seeded jobs
      ↓
5. One job ingestion source
      ↓
6. Recommendation engine
      ↓
7. Resume management
      ↓
8. LLM tailoring
      ↓
9. Application tracking
      ↓
10. Application automation
      ↓
11. Kafka integration
```

The first significant vertical slice is:

```text
POST /auth/register
       ↓
POST /auth/login
       ↓
JWT
       ↓
POST /search-profiles
       ↓
Seeded jobs
       ↓
Recommendation matching
       ↓
GET /job-recommendations
```

Once this flow works, the project has a stable foundation for crawling, resume tailoring, application automation, and Kafka.
