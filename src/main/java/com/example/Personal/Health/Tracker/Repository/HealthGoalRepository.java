package com.example.Personal.Health.Tracker.Repository;

import com.example.Personal.Health.Tracker.Entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HealthGoalRepository extends JpaRepository<Goal, Long> {
//    List<Goal> findByUserId(Long id);
}
