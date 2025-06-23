package com.online.busbooking.user_mangment_service.entity;


import com.online.busbooking.user_mangment_service.model.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String mobileNumber;

    private String email;

}
