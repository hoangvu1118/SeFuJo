package com.sefujo.job.source.greenhouse;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GreenhouseJob(
        Long id,
        String title,
        GreenhouseLocation location,

        @JsonProperty("absolute_url")
        String absoluteUrl,

        @JsonProperty("updated_at")
        OffsetDateTime updatedAt

) {
}
