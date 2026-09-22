package com.sefujo.common.health;

import com.sefujo.job.source.greenhouse.GreenhouseJob;
import com.sefujo.job.source.greenhouse.GreenhouseJobDetail;
import com.sefujo.job.source.greenhouse.GreenhouseJobsResponse;
import com.sefujo.job.source.greenhouse.GreenhouseLocation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/test")
public class GreenhouseTestController {

    private final WebClient greenhouseWebClient;

    public GreenhouseTestController(WebClient greenhouseWebClient) {
        this.greenhouseWebClient = greenhouseWebClient;
    }



    @GetMapping("/axon")
    public GreenhouseJobsResponse  getAxonJobs() {
        GreenhouseJobsResponse response = greenhouseWebClient
                .get()
                .uri("/v1/boards/axon/jobs")
                .retrieve()
                .bodyToMono(GreenhouseJobsResponse.class)
                .block();
        return response;
    }
}