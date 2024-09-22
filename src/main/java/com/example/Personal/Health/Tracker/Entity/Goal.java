package com.example.Personal.Health.Tracker.Entity;

import com.example.Personal.Health.Tracker.Enum.GoalType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Goal")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Enumerated
    private GoalType goalType;
    private Double targetValue;

    private Boolean achieved = false;
    private Boolean isActive = true;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;
    private LocalDate updatedAt;


}
