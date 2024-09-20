package com.example.Personal.Health.Tracker.Entity;

import com.example.Personal.Health.Tracker.Enum.GoalType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name = "Goal")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private Users users;

    @Enumerated
    private GoalType goalType;
    private Double targetValue;
    private LocalDate targetDate;

    private Boolean achieved = false;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

}
