package com.se.fawry.controller;

import com.se.fawry.enums.Role;
import com.se.fawry.model.entity.User;
import com.se.fawry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;

    }

    record LoginRequest(String email, String password){}
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.signIn(loginRequest.email, loginRequest.password);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

    record SignupRequest(String username, String email, String password, Role role){}
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequest signupRequest) {
        User user = userService.signUp(signupRequest.username, signupRequest.email, signupRequest.password, signupRequest.role);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}
