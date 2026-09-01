# Job Ingestion / Crawling Strategy Notes

> This document is intentionally a reference for later.  
> For now, follow the main implementation roadmap first and get the basic application working before building the crawling system.

---

# 1. Core Recommendation

For the MVP, do **not** start by building a generic web crawler.

A better approach is:

```text
Official/public ATS API
        ↓
Structured JSON-LD
        ↓
Plain HTTP + HTML parsing
        ↓
Undocumented JSON/XHR endpoint
        ↓
Playwright/browser automation
```

Use Playwright only as a fallback.

The general goal is not:

> Build a crawler.

It is:

> Build a Job Ingestion System with multiple source adapters.

---

# 2. Why ATS APIs Are Important

Many companies do not build their own career systems.

Their career pages are often powered by platforms such as:

- Greenhouse
- Lever
- Ashby
- SmartRecruiters
- Workable
- Workday

Instead of implementing:

```text
BoschCrawler
NABCrawler
GrabCrawler
CompanyXCrawler
```

prefer:

```text
GreenhouseSource
LeverSource
AshbySource
SmartRecruitersSource
WorkableSource
```

One adapter may support many companies.

---

# 3. Recommended Source Priority

| Priority | Method | Recommendation |
|---|---|---|
| 1 | Official/public ATS API | Best starting point |
| 2 | Schema.org JobPosting JSON-LD | Strong generic fallback |
| 3 | Plain HTTP + HTML parsing | Use when structured data is unavailable |
| 4 | Existing JSON/XHR endpoint | Useful but may be undocumented |
| 5 | Playwright | Last resort |

---

# 4. Best First Provider: Greenhouse

Greenhouse exposes public job-board endpoints.

Conceptually:

```http
GET https://boards-api.greenhouse.io/v1/boards/{board_token}/jobs?content=true
```

This can return structured job data including:

- job ID
- title
- location
- job description
- source URL
- update time

The first source adapter could look conceptually like:

```java
public class GreenhouseJobSource implements JobSource {

    @Override
    public List<RawJobPosting> fetchJobs(JobSourceConfig config) {

        // call Greenhouse API
        // deserialize JSON
        // convert results to RawJobPosting

        return jobs;
    }
}
```

This should be easier and more reliable than browser automation.

---

# 5. Second Provider: Lever

Lever also exposes public job postings as structured data.

Conceptually:

```http
GET https://api.lever.co/v0/postings/{site}?mode=json
```

It can provide information such as:

- posting ID
- title
- team
- location
- employment commitment
- description
- hosted URL
- apply URL

The important architectural test is:

```text
Greenhouse ──┐
             ├── RawJobPosting ──→ same ingestion pipeline
Lever ───────┘
```

If adding Lever requires rewriting the whole ingestion flow, the abstraction likely needs improvement.

---

# 6. Other Useful ATS Sources

Potential later adapters:

```text
AshbySource
SmartRecruitersSource
WorkableSource
WorkdaySource
```

Ashby, SmartRecruiters, and Workable expose structured job data through APIs or public endpoints.

Workday is very common, but its public career-site JSON endpoints are less cleanly documented as a public job-board contract, so it is better treated as a later adapter.

---

# 7. JobSource Abstraction

A basic interface could be:

```java
public interface JobSource {

    List<RawJobPosting> fetchJobs(JobSourceConfig config);

    PlatformType supports();
}
```

Implementations:

```text
GreenhouseJobSource
LeverJobSource
AshbyJobSource
```

The domain layer should not care which platform produced the data.

---

# 8. RawJobPosting

All providers should be converted into one internal raw format.

Example:

```java
public record RawJobPosting(
    String externalId,
    String title,
    String companyName,
    String description,
    String location,
    String department,
    String employmentType,
    String sourceUrl,
    String applyUrl,
    Instant publishedAt,
    Instant updatedAt,
    String rawPayload
) {}
```

Different providers may use different field names:

```text
Greenhouse: absolute_url
Lever: hostedUrl
Ashby: jobUrl
```

but the rest of the application should only work with:

```text
RawJobPosting.sourceUrl
```

---

# 9. JobSourceConfig

The application may eventually need configuration describing how to fetch jobs for a specific company.

Conceptually:

```text
JobSourceConfig
-------------------------
id
company_id
platform_id

career_url
external_identifier

active

last_synced_at
last_success_at

created_at
updated_at
```

Example:

```text
company = Acme
platform = GREENHOUSE

career_url =
https://boards.greenhouse.io/acme

external_identifier =
acme
```

Meaning:

> Where and how should the system retrieve this company's jobs?

This is different from `JobPosting`.

`JobPosting` represents a specific job advertisement that was already discovered.

---

# 10. Generic JSON-LD Extraction

Many job pages include Schema.org structured data:

```html
<script type="application/ld+json">
```

containing data such as:

```json
{
  "@context": "https://schema.org",
  "@type": "JobPosting",
  "title": "Backend Engineer",
  "description": "...",
  "datePosted": "...",
  "hiringOrganization": {
    "name": "Acme"
  }
}
```

A generic fallback could therefore do:

```text
GET job page
    ↓
Look for JobPosting JSON-LD
    ↓
Parse JSON
    ↓
RawJobPosting
```

This is usually more stable than relying on CSS selectors.

---

# 11. HTML Crawling

If there is:

```text
No API
No JSON-LD
```

then use plain HTTP + HTML parsing.

In Java, Jsoup is a natural option:

```java
Document document = Jsoup.connect(url).get();
```

Then extract required fields.

However, this becomes site-specific because different companies may use different HTML structures.

Therefore it should not be the first ingestion method.

---

# 12. JavaScript-Heavy Sites

Some sites return almost empty HTML and load job data afterward using JavaScript.

Before using Playwright, inspect whether the frontend calls a JSON endpoint.

Possible flow:

```text
Career page
   ↓
JavaScript executes
   ↓
fetch/XHR request
   ↓
JSON endpoint
```

If such an endpoint exists and its use is appropriate, calling the structured endpoint is generally lighter than browser automation.

---

# 13. Playwright as Last Resort

Use Playwright only when:

```text
No usable API
AND
No JSON-LD
AND
Static HTML is insufficient
AND
Browser rendering is genuinely required
```

Playwright is heavier because it requires:

```text
start browser
load HTML
load scripts
execute JavaScript
wait for rendering
query DOM
```

For a small number of pages this may be acceptable, but it becomes expensive and fragile at scale.

---

# 14. Recommended Future Architecture

```text
                  JobIngestionScheduler
                           │
                           ▼
                   JobSourceConfig
                           │
                           ▼
                    SourceRegistry
                           │
        ┌──────────────────┼─────────────────┐
        │                  │                 │
        ▼                  ▼                 ▼
 GreenhouseSource      LeverSource       AshbySource
        │                  │                 │
        └──────────────────┼─────────────────┘
                           ▼
                    RawJobPosting
                           │
                           ▼
                     Normalizer
                           │
                           ▼
                     Deduplicator
                           │
                           ▼
                 Job + JobPosting
                           │
                           ▼
                      PostgreSQL
```

Later:

```text
                    SourceRegistry
                          │
       ┌──────────────────┼───────────────────┐
       ▼                  ▼                   ▼
    ATS APIs          JSON-LD Parser      HTML Parser
                                             │
                                             ▼
                                       Playwright
                                       if required
```

---

# 15. Recommended Future Implementation Order

Do not implement all of this now.

When the crawling phase begins, use this order:

```text
STEP 1
Greenhouse
```

Get this working:

```text
Greenhouse API
      ↓
RawJobPosting
      ↓
JobIngestionService
      ↓
Job + JobPosting
      ↓
PostgreSQL
```

Then:

```text
STEP 2
Lever
```

Use Lever to verify that the adapter architecture is reusable.

Then:

```text
STEP 3
Ashby
```

Then:

```text
STEP 4
Generic JSON-LD parser
```

Then:

```text
STEP 5
Workday / other structured sources
```

Then:

```text
STEP 6
HTML parsing
```

Then only if necessary:

```text
STEP 7
Playwright
```

---

# 16. Important Scope Decision

For now, do **not** work on:

- multiple ATS adapters
- generic crawling
- Playwright
- Kafka
- sophisticated deduplication
- crawling schedules
- large-scale ingestion

Continue with the main implementation roadmap first:

```text
1. Project setup
2. Authentication
3. SearchProfile
4. Job domain with seeded test data
5. Basic recommendation flow
```

The crawling system can be added once the rest of the application has something concrete to ingest jobs into.

---

# 17. First Crawling Milestone When You Return

The first crawling milestone should be:

> Fetch jobs from one Greenhouse-powered company, convert them into `RawJobPosting`, and persist them correctly as `Job` and `JobPosting`.

Nothing more is required for the first version.
