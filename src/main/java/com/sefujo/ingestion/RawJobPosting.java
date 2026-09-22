package com.sefujo.ingestion;

import java.time.OffsetDateTime;

public record RawJobPosting(
        String sourceKey, // e.g axon
        String externalId,
        String companyName,
        String title,
        String location,
        String description,
        String sourceUrl,
        OffsetDateTime sourceUpdatedAt,
        OffsetDateTime sourcePublishedAt
) {}
