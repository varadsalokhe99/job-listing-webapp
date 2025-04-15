package com.backend.demo.controllers;


import com.backend.demo.entities.*;
import com.backend.demo.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // Create Job
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/user/{userId}/job")
    public ResponseEntity<?> createJob(@RequestBody Job job,@PathVariable Long userId){
        try {
        Job newJob = jobService.createJob(job, userId);
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
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id){
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // Update Job
    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{jobId}")
    public ResponseEntity<Job> updateJob(@PathVariable Long jobId, @RequestParam Long userId, @RequestBody Job job){
        return ResponseEntity.ok(jobService.updateJobById(jobId,userId, job));
    }

    // Delete job
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id){
        jobService.deleteJobById(id);
        return ResponseEntity.ok("Job deleted Successfully");
    }

}
