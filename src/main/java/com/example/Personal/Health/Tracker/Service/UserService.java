package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Entity.User;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
import com.example.Personal.Health.Tracker.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    /**
     * Register user with cred and hash the Password
     *
     * @param user
     * @return
     */
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**verify user when login return access token
     *
     * @param user
     * @return
     */
    public String verifyUser(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated())
            return jwtUtil.generateToken(user.getUsername());
        throw new  UsernameNotFoundException("User Not Authenticated");
    }
}
