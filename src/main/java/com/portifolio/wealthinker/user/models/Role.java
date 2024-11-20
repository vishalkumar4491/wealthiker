package com.portifolio.wealthinker.user.models;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., ROLE_USER, ROLE_ADMIN

    @Column
    private String description; // Optional: A description of the role's purpose.

    @ManyToMany(mappedBy = "roles")
    // @JsonIgnore
    private Set<User> users; // Back-reference to users with this role

}
