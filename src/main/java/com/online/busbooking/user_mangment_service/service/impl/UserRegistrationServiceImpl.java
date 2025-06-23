package com.online.busbooking.user_mangment_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.busbooking.user_mangment_service.entity.UserEntity;
import com.online.busbooking.user_mangment_service.model.SuccessResponseDTO;
import com.online.busbooking.user_mangment_service.model.UserRegisterDTO;
import com.online.busbooking.user_mangment_service.repository.UserRegisterRepository;
import com.online.busbooking.user_mangment_service.service.UserRegistrationService;
import org.springframework.stereotype.Service;


@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRegisterRepository userRegisterRepository;
    private final ObjectMapper objectMapper;

    public UserRegistrationServiceImpl(UserRegisterRepository userRegisterRepository , ObjectMapper objectMapper){
        this.userRegisterRepository = userRegisterRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public SuccessResponseDTO registerUserDetails(UserRegisterDTO userRegisterDTO) {
        SuccessResponseDTO response = new SuccessResponseDTO();
        UserEntity userEntity = objectMapper.convertValue(userRegisterDTO, UserEntity.class);
        userRegisterRepository.save(userEntity);
        response.setSuccess(true);
        response.setMessage("User has been registered successfully");
        response.setDescription("User has been registered successfully");
        return response;

    }
}
