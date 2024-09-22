package com.example.Personal.Health.Tracker.Repository;

import com.example.Personal.Health.Tracker.Entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface HealthGoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUsersId(Long id);

    List<Goal> findByUsersIdAndIsActiveTrue(Long userId);
}
