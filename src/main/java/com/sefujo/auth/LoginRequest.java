package com.sefujo.auth;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class LoginRequest {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Must be a valid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
