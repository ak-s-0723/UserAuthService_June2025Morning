package org.example.userauthservice_june2025morning.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateTokenRequest {
    private String token;
    private Long userId;
}
