package com.backend.demo.controllers;


import com.backend.demo.entities.*;
import com.backend.demo.services.JobService;
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
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    // Create Job
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody Job job,@AuthenticationPrincipal UserDetails userDetails){
        try {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Job newJob = jobService.createJob(job, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newJob);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    }

    //Get All Jobs
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs(){
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    //get jobs by id
    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJobById(@PathVariable Long jobId){
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    // Update Job
    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{jobId}")
    public ResponseEntity<Job> updateJob(@PathVariable Long jobId, @AuthenticationPrincipal UserDetails userDetails, @RequestBody Job job){
        User user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(jobService.updateJobById(jobId,user.getId(), job));
    }

    // Delete job
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @DeleteMapping("/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId, @AuthenticationPrincipal UserDetails userDetails){

        User user = userService.getUserByEmail(userDetails.getUsername());

        if(user.getRole() == Role.ADMIN){
            jobService.deleteJobById(jobId);
        }else {
        jobService.deleteJob(jobId, user.getId());
        }
        return ResponseEntity.ok("Job deleted Successfully");
    }

}
