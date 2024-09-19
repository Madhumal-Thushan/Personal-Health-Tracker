package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Entity.User;
import com.example.Personal.Health.Tracker.Service.AuthUserService;
import com.example.Personal.Health.Tracker.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {

        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.verifyUser(user);
    }
}
