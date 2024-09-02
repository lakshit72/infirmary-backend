package com.infirmary.backend.configuration.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="RoleUserMap")
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    @OneToOne(name )
    private int id;

    
}