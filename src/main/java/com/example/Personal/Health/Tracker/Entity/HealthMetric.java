package com.example.Personal.Health.Tracker.Entity;

import com.example.Personal.Health.Tracker.Enum.MetricType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;


@Entity
@Data
@Table(name = "health_metrics")
public class HealthMetric {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated
    private MetricType metricType;

    private Float value;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDate createdDate;

    @Column(name = "updated_at")
    private LocalDate updatedDate;

}
