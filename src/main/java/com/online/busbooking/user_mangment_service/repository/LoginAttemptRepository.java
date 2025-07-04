package com.online.busbooking.user_mangment_service.repository;

import com.online.busbooking.user_mangment_service.entity.LoginAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttemptEntity, Integer> {
    Optional<LoginAttemptEntity> findByEmailId(String emailId);
}
