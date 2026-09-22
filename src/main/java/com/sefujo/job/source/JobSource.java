package com.sefujo.job.source;

import com.sefujo.ingestion.RawJobPosting;
import com.sefujo.ingestion.RawJobSummary;

import java.util.List;

public interface JobSource {
    // sourceKey = board_token (e.g axon)

    List<RawJobSummary> fetchJobSummaries(String sourceKey);

    RawJobPosting fetchJobDetail(
            String sourceKey,
            String externalId
    );
}
