package com.backend.demo.repositories;

import com.backend.demo.dtos.ApplicationInfo;
import com.backend.demo.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByJobId (Long job);

    List<Application> findByUserId (Long userId);

    boolean existsByUserAndJob(User user, Job job);

    @Query("SELECT " +
            "a.id AS id, " +
            "a.resumeLink AS resumeLink, " +
            "a.status AS status, " +
            "a.coverLetter AS coverLetter, " +
            "a.appliedAt AS appliedAt, " +
            "j.title AS jobTitle, " +
            "employer.fullName AS employerName, " +
            "job_seeker.fullName AS jobSeekerName " +
            "FROM Application a " +
            "JOIN a.job j " +
            "JOIN j.postedBy employer " +
            "JOIN a.user job_seeker")
    List<ApplicationInfo> findAllApplicationDetails();

}
