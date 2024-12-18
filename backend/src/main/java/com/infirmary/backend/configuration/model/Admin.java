package com.infirmary.backend.configuration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="admin")
public class Admin {
    @Id
    @Column(name = "admin_email")
    private String adminEmail;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password")
    private String password;
}
