package com.sefujo.job.controller;

import com.sefujo.ingestion.JobIngestionService;
import com.sefujo.ingestion.RawJobPosting;
import com.sefujo.job.source.JobSource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/ingestion")
public class JobIngestionController {
    private final JobIngestionService ingestionService;
    JobSource greenhouseSource;

    public JobIngestionController(
            JobIngestionService ingestionService,
            @Qualifier("greenhouseJobSource") JobSource greenhouseSource
    ) {
        this.ingestionService = ingestionService;
        this.greenhouseSource = greenhouseSource;
    }

    @PostMapping("/greenhouse/{boardToken}")
    public ResponseEntity<List<RawJobPosting>> ingest(
            @PathVariable String boardToken
    ) {
        List<RawJobPosting> jobs =
                ingestionService.ingest(greenhouseSource, boardToken);
        return ResponseEntity.ok(jobs);
    }
}