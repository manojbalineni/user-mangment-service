package com.online.busbooking.user_mangment_service.service;

import com.online.busbooking.user_mangment_service.model.LoginRequestDTO;
import com.online.busbooking.user_mangment_service.model.LoginResponseDTO;
import com.online.busbooking.user_mangment_service.model.SuccessResponseDTO;
import com.online.busbooking.user_mangment_service.model.UserRegisterDTO;
import jakarta.mail.MessagingException;

public interface UserRegistrationService {
    public SuccessResponseDTO registerUserDetails(UserRegisterDTO userRegisterDTO) throws MessagingException;
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO);

    public SuccessResponseDTO activateAccount(String token);
}
