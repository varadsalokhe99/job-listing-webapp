package com.backend.demo.services;

import com.backend.demo.entities.Role;
import com.backend.demo.entities.User;
import com.backend.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Save new User
    public User registerUser(User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Get all Users
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    // Get User by id
    public User getUserById(Long id){
            return userRepository.findById(id).
                    orElseThrow(()-> new RuntimeException("User not found by with id : " + id));
    }

    // Get User by email id
    public User getUserByEmail(String email){
        return  userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User not found by this "+email+" email"));
    }

    // Get user by Role
    public List<User> getUsersByRole(Role role){
        return userRepository.findByRole(role);
    }

    // Update User details
    public User updateUser(Long id, User userDetails){
        User user = getUserById(id);

        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        if (!userDetails.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    // Delete User
    public void deleteUserById(Long id){
       userRepository.deleteById(id);
    }


}
