package com.example.Personal.Health.Tracker.Entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

}
