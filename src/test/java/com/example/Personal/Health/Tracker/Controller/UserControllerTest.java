package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Service.Auth.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        // Arrange
        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("testPass");

        Users createdUser = new Users();
        createdUser.setId(1L);
        createdUser.setUsername("testUser");

        when(userService.registerUser(user)).thenReturn(createdUser);

        // Act
        Users response = userController.register(user);

        // Assert
        assertThat(response).isEqualTo(createdUser);
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testLoginSuccessful() {
        // Arrange
        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("testPass");

        String expectedResponse = "Login successful";

        when(userService.verifyUser(user)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = userController.login(user);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(userService, times(1)).verifyUser(user);
    }

    @Test
    void testLoginFailure() {
        // Arrange
        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("wrongPass");

        when(userService.verifyUser(user)).thenThrow(new RuntimeException("Login failed"));

        // Act
        ResponseEntity<String> response = userController.login(user);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("An error occurred while processing your request.");
        verify(userService, times(1)).verifyUser(user);
    }
}
