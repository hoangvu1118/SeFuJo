package com.sefujo.job.source.greenhouse;

import java.util.List;

public record GreenhouseJobsResponse(
        List<GreenhouseJob> jobs
) {
}
