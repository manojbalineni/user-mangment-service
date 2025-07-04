package com.online.busbooking.user_mangment_service.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_login_attempt")
public class LoginAttemptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private String emailId;
    private boolean accountLocked = false;
    private int failedAttempts;
    private LocalDateTime lockedTime;
    private LocalDateTime lastModifiedTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public LocalDateTime getLockedTime() {
        return lockedTime;
    }

    public void setLockedTime(LocalDateTime lockedTime) {
        this.lockedTime = lockedTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
