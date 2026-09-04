package com.sefujo.searchprofile;

import com.sefujo.common.exception.ResourceNotFound;
import com.sefujo.common.exception.SearchProfileAlreadyExist;
import com.sefujo.common.exception.UserNotAuthenticated;
import com.sefujo.common.security.CustomUserDetail;
import com.sefujo.searchprofile.dto.CreateSearchProfileRequest;
import com.sefujo.searchprofile.dto.SearchProfileResponse;
import com.sefujo.searchprofile.dto.UpdateSearchProfileRequest;
import com.sefujo.user.User;
import com.sefujo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class SearchProfileService {
    SearchProfileRepository searchProfileRepository;
    UserRepository userRepository;

    private SearchProfileResponse getSearchProfileResponse(SearchProfile saved) {
        SearchProfileResponse response = new SearchProfileResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setLevel(saved.getLevel());

        response.setEmploymentTypes(saved.getEmploymentTypes());
        response.setJobTitles(saved.getJobTitles());
        response.setLocations(saved.getLocations());
        response.setSkills(saved.getSkills());
        response.setWorkplaceTypes(saved.getWorkplaceTypes());

        return response;
    }

    public SearchProfileResponse createSearchProfile(CreateSearchProfileRequest request) {

        LocalDateTime now = LocalDateTime.now();
        long userId = getCurrentUserId();

        SearchProfile searchProfileCheck = searchProfileRepository.findByUserId(userId).orElse(null);
        if (searchProfileCheck != null) {
            throw new SearchProfileAlreadyExist("Search Profile Already Exist, please Edit/ Delete the previous version");
        }

        User currentUser = userRepository.findById(userId).orElse(null);

        SearchProfile searchProfile = new SearchProfile();
        searchProfile.setUser(currentUser);
        searchProfile.setName(request.getName());
        searchProfile.setLevel(request.getLevel());
        searchProfile.setCreatedAt(now);
        searchProfile.setUpdatedAt(now);
        searchProfile.setEmploymentTypes(request.getEmploymentTypes());
        searchProfile.setJobTitles(request.getJobTitles());
        searchProfile.setLocations(request.getLocations());
        searchProfile.setSkills(request.getSkills());
        searchProfile.setWorkplaceTypes(request.getWorkplaceTypes());

        SearchProfile saved = searchProfileRepository.save(searchProfile);

        return getSearchProfileResponse(saved);
    }


    public SearchProfileResponse getMySearchProfile() {
        long userId = getCurrentUserId();
        SearchProfile searchProfile = searchProfileRepository.findByUserId(userId).orElse(null);
        if (searchProfile == null) {
            throw new ResourceNotFound("Search Profile Not Found from UserID: " + userId);
        }
        return getSearchProfileResponse(searchProfile);
    }

    public SearchProfileResponse updateSearchProfile(UpdateSearchProfileRequest request) {
        LocalDateTime now = LocalDateTime.now();
        long userId = getCurrentUserId();
        SearchProfile profile = searchProfileRepository.findByUserId(userId).orElse(null);
        if (profile == null) {
            throw new ResourceNotFound("Search Profile Not Found from UserID: " + userId);
        }
        if (request.getName() != null) {
            profile.setName(request.getName());
        }

        if (request.getLevel() != null) {
            profile.setLevel(request.getLevel());
        }

        if (request.getSkills() != null) {
            profile.setSkills(request.getSkills());
        }

        if (request.getLocations() != null) {
            profile.setLocations(request.getLocations());
        }

        if (request.getJobTitles() != null) {
            profile.setJobTitles(request.getJobTitles());
        }

        if (request.getEmploymentTypes() != null) {
            profile.setEmploymentTypes(request.getEmploymentTypes());
        }
        if (request.getWorkplaceTypes() != null) {
            profile.setWorkplaceTypes(request.getWorkplaceTypes());
        }
        SearchProfile saved = searchProfileRepository.save(profile);
        return getSearchProfileResponse(saved);
    }

    public void deleteSearchProfile() {
        long userId = getCurrentUserId();
        SearchProfile profile = searchProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFound("Search Profile Not Found from UserID: " + userId));

        searchProfileRepository.delete(profile);
    }

    private long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new UserNotAuthenticated("Username not Authenticated yet");
        }
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        if(userDetail == null) {
            throw new UsernameNotFoundException("Username not Authenticated yet");
        }
        return userDetail.getId();
    }
}
