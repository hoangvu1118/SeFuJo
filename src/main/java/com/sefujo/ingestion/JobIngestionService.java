package com.sefujo.ingestion;

import com.sefujo.job.JobLocationFilter;
import com.sefujo.job.source.JobSource;
import com.sefujo.job.source.greenhouse.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JobIngestionService {
    private final GreenhouseJobSource greenhouseSource;
    private final JobLocationFilter locationFilter;
    private final GreenhouseJobMapper mapper;

    public List<RawJobPosting> ingest(
            JobSource source,
            String sourceKey
    ) {
        List<RawJobSummary> summaries =
                source.fetchJobSummaries(sourceKey);

        List<RawJobPosting> results = new ArrayList<>();

        for (RawJobSummary summary : summaries) {
            if (!locationFilter.isTargetLocation(summary.location())) {
                continue;
            }

            results.add(
                    source.fetchJobDetail(
                            sourceKey,
                            summary.externalId()
                    )
            );
        }

        return results;
    }
}
