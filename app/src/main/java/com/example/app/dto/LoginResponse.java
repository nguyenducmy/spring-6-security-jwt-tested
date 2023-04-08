package com.example.app.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String status;
    private String jwt;
}
