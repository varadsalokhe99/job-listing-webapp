package com.backend.demo.controllers;

import com.backend.demo.entities.User;
import com.backend.demo.payload.request.LoginRequest;
import com.backend.demo.payload.response.LoginResponse;
import com.backend.demo.security.jwt.JwtUtils;
import com.backend.demo.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    // Login API
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Authenticate user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Fetch user from database
        User user = userService.getUserByEmail(loginRequest.getEmail());

        // Generate JWT token with email and role
        String jwt = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

        // Return login response with token and user info
        return ResponseEntity.ok(
                new LoginResponse(jwt, user.getId(), user.getFullName(), user.getEmail(), user.getRole())
        );
    }
}
