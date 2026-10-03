package com.sefujo.user;

import com.sefujo.common.exception.UserNotAuthenticated;
import com.sefujo.common.security.CustomUserDetail;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public long getCurrentUserId(){
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

    public User getUserById(long id){
        return userRepository.findById(id).orElse(null);
    }
}
