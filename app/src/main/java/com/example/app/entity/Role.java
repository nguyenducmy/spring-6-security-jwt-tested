package com.example.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String role;
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;
}
