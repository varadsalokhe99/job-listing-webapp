package com.backend.demo.services;

import com.backend.demo.dtos.ApplicationInfo;
import com.backend.demo.entities.*;
import com.backend.demo.repositories.ApplicationRepository;
import com.backend.demo.repositories.JobRepository;
import com.backend.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;


    // Submit Job application
    public Application applyForJob (Application applicationDetails, Long userId, Long jobId){
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job Not found by id: "+jobId));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found by id: "+userId));

        if(user.getRole() != Role.JOB_SEEKER){
            throw new RuntimeException("Only Job seekers can allowed apply for the jobs!!!");
        }

        applicationDetails.setJob(job);
        applicationDetails.setUser(user);
        applicationDetails.setStatus("PENDING");

        return applicationRepository.save(applicationDetails);
    }

    // Get custom data from Application
    public List<ApplicationInfo> getCustomInfo() {
        return applicationRepository.findAllApplicationDetails();
    }

    // Get all applications
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    //Get Application by job id
    public List<Application> getApplicationsBYJobId(Long jobId){
        return applicationRepository.findByJobId(jobId);
    }

    // Get applications by user id
    public List<Application> getApplicationByUserId (Long userId){

        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId
                ));
        if(user.getRole() != Role.JOB_SEEKER){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only Job Seekers can have applications."
            );
        }

        return applicationRepository.findByUserId(userId);
    }

    // Update application Status (Accepted or Rejected)
    public Application updateApplicationStatus (Long applicationId, String status, Long userId){

        Application application = applicationRepository.findById(applicationId).
                orElseThrow(() -> new RuntimeException("Application not found by id : "+applicationId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getRole() != Role.EMPLOYER && user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only Employer or Admin can update application status.");
        }

        // check to ensure employer is the one who posted the job
        if (user.getRole() == Role.EMPLOYER) {
            if (!application.getJob().getPostedBy().getId().equals(userId)) {
                System.out.print(application.getJob().getPostedBy().getId());
                throw new RuntimeException("You can only update applications for jobs posted by you.");
            }
        }

        application.setStatus(status);
        return applicationRepository.save(application);
    }

    // Delete application by ID
    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id).
                orElseThrow(() -> new RuntimeException("There is no application with id: "+id));
        applicationRepository.deleteById(id);
    }
 }
