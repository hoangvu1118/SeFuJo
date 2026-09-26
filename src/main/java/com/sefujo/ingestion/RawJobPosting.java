package com.sefujo.ingestion;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record RawJobPosting(
        String sourceKey, // e.g axon
        String externalId,
        String companyName,
        String title,
        String location,
        String description,
        String sourceUrl,
        LocalDateTime sourceUpdatedAt,
        LocalDateTime sourcePublishedAt
) {}
