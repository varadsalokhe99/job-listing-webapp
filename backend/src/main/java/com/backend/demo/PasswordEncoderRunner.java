package com.backend.demo;

import com.backend.demo.entities.User;
import com.backend.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class PasswordEncoderRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        for (User user : userRepository.findAll()) {
            String currentPassword = user.getPassword();

            // Check if it's already BCrypt encoded (starts with $2a or $2b)
            if (!currentPassword.startsWith("$2a$") && !currentPassword.startsWith("$2b$")) {
                String encodedPassword = passwordEncoder.encode(currentPassword);
                user.setPassword(encodedPassword);
                userRepository.save(user);
                System.out.println("Encoded password for: " + user.getEmail());
            } else {
                System.out.println("Already encoded: " + user.getEmail());
            }
        }
        System.out.println("Password encoding update completed!");
    }
}
