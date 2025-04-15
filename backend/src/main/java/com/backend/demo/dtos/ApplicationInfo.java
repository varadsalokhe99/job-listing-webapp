package com.backend.demo.dtos;

import java.time.LocalDateTime;

public interface ApplicationInfo {
    Long getId();
    String getResumeLink();
    String getStatus();
    String getCoverLetter();
    LocalDateTime getAppliedAt();
    String getJobTitle();
    String getEmployerName();
    String getJobSeekerName();
}
