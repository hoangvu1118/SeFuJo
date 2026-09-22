package com.sefujo.ingestion;

import java.time.OffsetDateTime;

public record RawJobSummary(
        String externalId,
        String title,
        String location,
        String sourceUrl,
        OffsetDateTime sourceUpdatedAt
) {}

