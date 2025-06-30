package com.online.busbooking.user_mangment_service.repository;

import com.online.busbooking.user_mangment_service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegisterRepository extends JpaRepository<UserEntity, Long>                                                                                                                                                                                                                                                                                             {
    UserEntity findByEmail(String email);
}
