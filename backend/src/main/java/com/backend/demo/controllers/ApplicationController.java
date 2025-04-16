package com.backend.demo.controllers;

import com.backend.demo.dtos.ApplicationInfo;
import com.backend.demo.entities.Application;

import com.backend.demo.entities.Role;
import com.backend.demo.entities.User;
import com.backend.demo.services.ApplicationService;
import com.backend.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserService userService;

    // Apply for Jobs
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/{jobId}/user")
    public ResponseEntity<Application> applyForJob(
            @PathVariable Long jobId, @AuthenticationPrincipal UserDetails userDetails, @RequestBody Application application
            ){
        User user = userService.getUserByEmail(userDetails.getUsername());

        Application newApplication = applicationService.applyForJob(application,user.getId(),jobId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newApplication);
    }

    // Get custom application list
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @GetMapping("/customData")
    public ResponseEntity<List<ApplicationInfo>> getCustomInfo(){
        return ResponseEntity.ok(applicationService.getCustomInfo());
    }

    // Get All applications
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications(){
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    // Get applications by Job id
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJobId (@PathVariable Long jobId){
        return ResponseEntity.ok(applicationService.getApplicationsBYJobId(jobId));
    }

    // Get applications by User id
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/user")
    public ResponseEntity<List<Application>> getApplicationsByUserId (@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(applicationService.getApplicationByUserId(user.getId()));
    }

    // Update Application Status
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER')")
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status,
           @AuthenticationPrincipal UserDetails userDetails){

        User user = userService.getUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId,status,user.getId()));
    }

    // Delete Application
    @PreAuthorize("hasRole('ADMIN') or hasRole('JOB_SEEKER')") // This make sure that only Employer or Admin can delete the application
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long applicationId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());

        if(user.getRole() == Role.ADMIN){
            applicationService.deleteApplicationById(applicationId);
        }else{
        applicationService.deleteApplication(applicationId, user.getId());
        }
        return ResponseEntity.ok("Application deleted successfully");
    }

}
