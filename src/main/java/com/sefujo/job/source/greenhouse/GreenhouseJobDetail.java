package com.sefujo.job.source.greenhouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GreenhouseJobDetail(
        Long id,
        String title,
        GreenhouseLocation location,

        @JsonProperty("absolute_url")
        String absoluteUrl,

        @JsonProperty("company_name")
        String companyName,

        String content,

        @JsonProperty("updated_at")
        OffsetDateTime updatedAt,

        @JsonProperty("first_published")
        OffsetDateTime firstPublished
) {}