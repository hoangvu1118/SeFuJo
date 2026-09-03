package com.sefujo.searchprofile;

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

    public SearchProfileResponse createSearchProfile(CreateSearchProfileRequest request) {

        LocalDateTime now = LocalDateTime.now();
        SearchProfile searchProfile = getSearchProfile(request, now);

        SearchProfile saved = searchProfileRepository.save(searchProfile);

        SearchProfileResponse response = new SearchProfileResponse();
        response.setId(saved.getId());
        response.setName(searchProfile.getName());
        response.setLevel(searchProfile.getLevel());
        
        response.setEmploymentTypes(saved.getEmploymentTypes());
        response.setJobTitles(saved.getJobTitles());
        response.setLocations(saved.getLocations());
        response.setSkills(saved.getSkills());
        response.setWorkplaceTypes(saved.getWorkplaceTypes());
        
        return response;
    }

    public SearchProfile getSearchProfile(CreateSearchProfileRequest request, LocalDateTime now) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new UserNotAuthenticated("Username not Authenticated yet");
        }
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        if(userDetail == null) {
            throw new UsernameNotFoundException("Username not Authenticated yet");
        }
        Long userId = userDetail.getId();
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

        return searchProfile;
    }

}
