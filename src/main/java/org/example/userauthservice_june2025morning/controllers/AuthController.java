package org.example.userauthservice_june2025morning.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_june2025morning.dtos.LoginRequestDto;
import org.example.userauthservice_june2025morning.dtos.SignupRequestDto;
import org.example.userauthservice_june2025morning.dtos.UserDto;
import org.example.userauthservice_june2025morning.dtos.ValidateTokenRequest;
import org.example.userauthservice_june2025morning.models.User;
import org.example.userauthservice_june2025morning.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignupRequestDto signupRequestDto) {
        User user = authService.signup(signupRequestDto.getName(), signupRequestDto.getEmail(), signupRequestDto.getPassword(), signupRequestDto.getPhoneNumber());
        return from(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Pair<User,String> response = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        UserDto userDto = from(response.a);
        String token = response.b;

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);

        return new ResponseEntity<>(userDto,headers,HttpStatus.OK);

    }

    @PostMapping("/validateToken")
    public Boolean validateToken(@RequestBody ValidateTokenRequest validateTokenRequest) {
       return null;
    }

    //ToDo : Write down wrapper for Logout and ForgetPassword API

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }
}
