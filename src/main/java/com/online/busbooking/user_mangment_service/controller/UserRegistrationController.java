package com.online.busbooking.user_mangment_service.controller;


import com.online.busbooking.user_mangment_service.model.LoginRequestDTO;
import com.online.busbooking.user_mangment_service.model.LoginResponseDTO;
import com.online.busbooking.user_mangment_service.model.SuccessResponseDTO;
import com.online.busbooking.user_mangment_service.model.UserRegisterDTO;
import com.online.busbooking.user_mangment_service.service.UserRegistrationService;
import jakarta.mail.MessagingException;
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
    public SuccessResponseDTO registerUser(@RequestBody UserRegisterDTO userRegisterDTO) throws MessagingException {
        return userRegistrationService.registerUserDetails(userRegisterDTO);
    }

    @PostMapping(value = "/login")
    public LoginResponseDTO loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        return userRegistrationService.loginUser(loginRequestDTO);

    }
}
