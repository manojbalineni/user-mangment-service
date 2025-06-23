package com.online.busbooking.user_mangment_service.service;

import com.online.busbooking.user_mangment_service.model.SuccessResponseDTO;
import com.online.busbooking.user_mangment_service.model.UserRegisterDTO;

public interface UserRegistrationService {
    public SuccessResponseDTO registerUserDetails(UserRegisterDTO userRegisterDTO);
}
