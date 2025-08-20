package org.example.userauthservice_june2025morning.controllers;

import org.example.userauthservice_june2025morning.dtos.UserDto;
import org.example.userauthservice_june2025morning.models.User;
import org.example.userauthservice_june2025morning.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public UserDto getUserDetails(@PathVariable Long id) {
        return from(userService.getUserDetailsBasedOnId(id));
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }
}
