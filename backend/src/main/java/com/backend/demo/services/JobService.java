package com.backend.demo.services;

import com.backend.demo.entities.*;
import com.backend.demo.repositories.JobRepository;
import com.backend.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    // Create New Job
    public Job createJob(Job job, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId:"+userId));

        if (user.getRole() != Role.EMPLOYER) {
            throw new IllegalArgumentException("Only employers can post jobs.");
        }
        job.setPostedBy(user);
        return jobRepository.save(job);
    }

    // Get All Jobs
    public List<Job> getAllJobs(){
        return jobRepository.findAll();
    }

    // Get Job by id
    public Job getJobById (Long id){
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    // Update Job By id
    public Job updateJobById(Long id,Long userId, Job jobDetails ){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found with id: "+userId));

        if(user.getRole() != Role.EMPLOYER){
            throw new IllegalArgumentException("Only Employer can update the Job");
        }
        Job job = getJobById(id);

        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setCompany(jobDetails.getCompany());
        job.setLocation(jobDetails.getLocation());
        job.setSalary(jobDetails.getSalary());

        return jobRepository.save(job);
    }

    // Delete Job by id
    public void deleteJobById(Long id){
         jobRepository.deleteById(id);
    }

}
