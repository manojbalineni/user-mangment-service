package com.online.busbooking.user_mangment_service.controller;


import com.online.busbooking.user_mangment_service.model.SuccessResponseDTO;
import com.online.busbooking.user_mangment_service.model.UserRegisterDTO;
import com.online.busbooking.user_mangment_service.service.UserRegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    public UserRegistrationController(UserRegistrationService userRegistrationService){
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping(value = "/register")
    public SuccessResponseDTO registerUser(@RequestBody UserRegisterDTO userRegisterDTO){
        return userRegistrationService.registerUserDetails(userRegisterDTO);
    }
}
