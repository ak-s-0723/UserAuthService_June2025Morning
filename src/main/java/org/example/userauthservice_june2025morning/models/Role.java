package org.example.userauthservice_june2025morning.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Role extends BaseModel {
    private String value;
}
