package com.backend.demo.controllers;

import com.backend.demo.dtos.ApplicationInfo;
import com.backend.demo.entities.Application;

import com.backend.demo.services.ApplicationService;
import com.backend.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PostMapping("/{jobId}/user/{userId}")
    public ResponseEntity<Application> applyForJob(
            @PathVariable Long jobId, @PathVariable Long userId, @RequestBody Application application
            ){
        Application newApplication = applicationService.applyForJob(application,userId,jobId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newApplication);
    }

    // Get custom application list
    @GetMapping("/customData")
    public ResponseEntity<List<ApplicationInfo>> getCustomInfo(){
        return ResponseEntity.ok(applicationService.getCustomInfo());
    }

    // Get All applications
    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications(){
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    // Get applications by Job id
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJobId (@PathVariable Long jobId){
        return ResponseEntity.ok(applicationService.getApplicationsBYJobId(jobId));
    }
    // Get applications by User id
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Application>> getApplicationsByUserId (@PathVariable Long userId){
        return ResponseEntity.ok(applicationService.getApplicationByUserId(userId));
    }

    // Update Application Status
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER')")
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status,
            @RequestParam Long userId){
        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId,status,userId));
    }

    // Delete Application
    @PreAuthorize("hasRole('ADMIN') or hasRole('JOB_SEEKER')") // This make sure that only Employer or Admin can delete the application
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok("Application deleted successfully");
    }

}
