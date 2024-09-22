package com.example.Personal.Health.Tracker.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    //added this for Notification and Reminders
    private String email;
}
