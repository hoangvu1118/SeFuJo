package com.sefujo.searchprofile;

import com.sefujo.searchprofile.dto.CreateSearchProfileRequest;
import com.sefujo.searchprofile.dto.SearchProfileResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/searchProfiles")
@AllArgsConstructor
public class SearchProfileController {
    SearchProfileService searchProfileService;

    @PostMapping()
    public ResponseEntity<SearchProfileResponse> createSearchProfile(
            @RequestBody CreateSearchProfileRequest request) {
        SearchProfileResponse searchProfileResponse = searchProfileService.createSearchProfile(request);
        return ResponseEntity.ok(searchProfileResponse);
    }
}