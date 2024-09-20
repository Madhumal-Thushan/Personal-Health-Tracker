package com.example.Personal.Health.Tracker.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<HealthMetric> healthMetricList;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<Goal> goalList;

}
