package com.sefujo.job.controller;

import com.sefujo.ingestion.JobIngestionService;
import com.sefujo.ingestion.RawJobPosting;
import com.sefujo.job.source.JobSource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingestion")
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
        return ResponseEntity.ok(
                ingestionService.ingest(greenhouseSource, boardToken)
        );
    }
}