package com.sefujo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private  String username;
    private String token;
    private Long userId;
}
