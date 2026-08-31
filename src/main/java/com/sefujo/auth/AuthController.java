package com.sefujo.auth;

import com.sefujo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/v1")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;
    private UserService userService;

//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
//        AuthResponse response = authService.authenticateUser(loginRequest);
//        return ResponseEntity.ok(response);
//    }

}
