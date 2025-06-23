package com.online.busbooking.user_mangment_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    private String title;
    private String name;
    private Gender gender;
    private String mobileNumber;
    private String email;

}
