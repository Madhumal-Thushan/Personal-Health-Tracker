package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
import com.example.Personal.Health.Tracker.Service.Auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService userService;

    @Mock
    private UserRepository userRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        String username = "testUser";
        Users user = new Users();
        user.setUsername(username);
        when(userRepo.findByUsername(username)).thenReturn(user);

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        verify(userRepo, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepo.findByUsername(username)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });

        assertThat(exception.getMessage()).isEqualTo("An unexpected error occurred while trying to find user: " + username);
        verify(userRepo, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_DatabaseError() {
        // Arrange
        String username = "testUser";
        when(userRepo.findByUsername(username)).thenThrow(new DataAccessException("Database error") {});

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });

        assertThat(exception.getMessage()).contains("Database error occurred while searching for user: " + username);
        verify(userRepo, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UnexpectedError() {
        // Arrange
        String username = "testUser";
        when(userRepo.findByUsername(username)).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });

        assertThat(exception.getMessage()).contains("An unexpected error occurred while trying to find user: " + username);
        verify(userRepo, times(1)).findByUsername(username);
    }
}
