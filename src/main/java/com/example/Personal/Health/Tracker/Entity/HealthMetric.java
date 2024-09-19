package com.example.Personal.Health.Tracker.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Entity
@Data
@Table(name = "health_metrics")
public class HealthMetric {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Double weight;
    private Double height;
    private Integer steps;
    private Double calories;

    @Column(name = "created_at", updatable = false)
    private Date createdDate = new Date();

    @Column(name = "updated_at")
    private Date updatedDate;
}
