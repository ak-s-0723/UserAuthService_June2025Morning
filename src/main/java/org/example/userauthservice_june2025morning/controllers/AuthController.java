package org.example.userauthservice_june2025morning.controllers;

import org.example.userauthservice_june2025morning.dtos.LoginRequestDto;
import org.example.userauthservice_june2025morning.dtos.SignupRequestDto;
import org.example.userauthservice_june2025morning.dtos.UserDto;
import org.example.userauthservice_june2025morning.dtos.ValidateTokenRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
//    signup
//    login
//    logout
//    forgetPassword
//    validateToken

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignupRequestDto signupRequestDto) {
        return null;
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return null;
    }

    @PostMapping("/validateToken")
    public Boolean validateToken(@RequestBody ValidateTokenRequest validateTokenRequest) {
       return null;
    }

    //ToDo : Write down wrapper for Logout and ForgetPassword API
}
