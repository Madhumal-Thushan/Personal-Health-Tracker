package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.HealthMetricDto;
import com.example.Personal.Health.Tracker.Dto.Response.HealthMetricResponse;
import com.example.Personal.Health.Tracker.Dto.UserDto;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthMetricRepository;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HealthMetricService {

    @Autowired
    private HealthMetricRepository healthMetricRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Add Health Metric by User
     * @param requestDto
     * @return
     */
    @Transactional
    public HealthMetricResponse addHealthMetrics(HealthMetricDto requestDto) {
        Users user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        HealthMetric metric = new HealthMetric();
        metric.setMetricType(requestDto.getMetricType());
        metric.setValue(requestDto.getValue());
        metric.setCreatedDate(LocalDate.now());
        metric.setUser(user);

        HealthMetric savedMetric = healthMetricRepository.save(metric);
        return convertToResponseDTO(savedMetric);
    }

    /**
     * Get All the Health Metrics by User
     * @param userId
     * @return
     */
    @Transactional
    public List<HealthMetricResponse> getHealthMetricsByUser(Long userId) {
        List<HealthMetric> healthMetricList = healthMetricRepository.findByUserId(userId);
        if(healthMetricList.isEmpty() ) {
            throw new ResourceNotFoundException("No Health Metrics Found for user ID " +userId);
        }
        return healthMetricList.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    /**
     * Get Single Health Metric By ID
     * @param id
     * @return
     */
    @Transactional
    public HealthMetricResponse getHealthMetricById(Long id) {
        HealthMetric metric = healthMetricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthMetric not found with id " + id));
        return convertToResponseDTO(metric);
    }

    /**
     * Update Health Metric By User
     * @param id
     * @param metric
     * @return
     */
    @Transactional
    public HealthMetricResponse updateHealthMetric(Long id, HealthMetricDto metric) {
        HealthMetric existingMetric = healthMetricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthMetric not found with id " + id));

        existingMetric.setMetricType(metric.getMetricType());
        existingMetric.setValue(metric.getValue());
        existingMetric.setUpdatedDate(LocalDate.now());
        healthMetricRepository.save(existingMetric);
        return convertToResponseDTO(existingMetric);
    }

    /**
     * This Private method Used to Convert according to Health Metric Response
     * @param metric
     * @return
     */
    private HealthMetricResponse convertToResponseDTO(HealthMetric metric) {
        HealthMetricResponse responseDTO = new HealthMetricResponse();
        responseDTO.setId(metric.getId());
        responseDTO.setMetricType(metric.getMetricType());
        responseDTO.setValue(metric.getValue());
        responseDTO.setCreatedDate(metric.getCreatedDate());

        UserDto userDTO = new UserDto();
        userDTO.setId(metric.getUser().getId());
        userDTO.setUsername(metric.getUser().getUsername());
        responseDTO.setUser(userDTO);

        return responseDTO;
    }
}
