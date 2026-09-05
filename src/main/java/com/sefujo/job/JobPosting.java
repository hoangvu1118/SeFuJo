package com.sefujo.job;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name="job_postings")
@Data
@Entity
public class JobPosting {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name="job_id")
    private long jobId;

    @Column(name="platform_id")
    private long platformId;

    @Column(name="external_job_id")
    private String externalJobId;

    private String status;
    @Column(name="source_published_at")
    private LocalDateTime sourcePublishedAt; // Track when the Platform publishes
    @Column(name="first_seen_at")
    private LocalDateTime firstSeenAt; // When the crawler first discover it
    @Column(name="last_seen_at")
    private LocalDateTime lastSeenAt; // When the crawler most recently confirmed the posting still exists
    @Column(name="closed_at")
    private LocalDateTime closedAt; // Determine when the posting is closed
    @Column(name="created_at")
    private LocalDateTime createdAt; // When the instance was first created in the database
    @Column(name="updated_at")
    private LocalDateTime updatedAt; // When the instance was most recently modified in the database
}
