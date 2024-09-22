package com.example.Personal.Health.Tracker.Service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
import com.example.Personal.Health.Tracker.Service.Auth.JWTService;
import com.example.Personal.Health.Tracker.Service.Auth.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("plainPassword");

        when(userRepository.save(any(Users.class))).thenReturn(user);

        Users registeredUser = userService.registerUser(user);

        assertThat(registeredUser.getUsername()).isEqualTo("testUser");
        assertThat(registeredUser.getPassword()).isNotEqualTo("plainPassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testVerifyUserSuccessful() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("plainPassword");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(user.getUsername())).thenReturn("mockedToken");

        String token = userService.verifyUser(user);

        assertThat(token).isEqualTo("mockedToken");
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testVerifyUserAuthenticationFailed() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("plainPassword");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new UsernameNotFoundException("User not found"));

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.verifyUser(user);
        });

        assertThat(exception.getMessage()).isEqualTo("User not found");
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testVerifyUserInvalidCredentials() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("wrongPassword");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("user not found"));

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.verifyUser(user);
        });

        assertThat(exception.getMessage()).isEqualTo("user not found");
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
