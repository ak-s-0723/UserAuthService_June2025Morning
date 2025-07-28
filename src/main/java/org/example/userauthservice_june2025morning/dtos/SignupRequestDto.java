package org.example.userauthservice_june2025morning.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
}
