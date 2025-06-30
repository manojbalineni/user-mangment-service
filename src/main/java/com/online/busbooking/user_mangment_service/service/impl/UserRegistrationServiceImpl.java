package com.online.busbooking.user_mangment_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.busbooking.user_mangment_service.entity.UserEntity;
import com.online.busbooking.user_mangment_service.exception.UserAlreadyExistsException;
import com.online.busbooking.user_mangment_service.model.SuccessResponseDTO;
import com.online.busbooking.user_mangment_service.model.UserRegisterDTO;
import com.online.busbooking.user_mangment_service.repository.UserRegisterRepository;
import com.online.busbooking.user_mangment_service.service.UserRegistrationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRegisterRepository userRegisterRepository;
    private final ObjectMapper objectMapper;
    private final EmailServiceImpl emailService;


    public UserRegistrationServiceImpl(UserRegisterRepository userRegisterRepository ,
                                       ObjectMapper objectMapper,
                                       EmailServiceImpl emailService)
                                       {
        this.userRegisterRepository = userRegisterRepository;
        this.objectMapper = objectMapper;
        this.emailService = emailService;

    }


    @Override
    public SuccessResponseDTO registerUserDetails(UserRegisterDTO userRegisterDTO) throws MessagingException {
        SuccessResponseDTO response = new SuccessResponseDTO();
        UserEntity userDetails = userRegisterRepository.findByEmail(userRegisterDTO.getEmail());
        if(userDetails != null){
            throw new UserAlreadyExistsException("US001" , "User with the email already exists" , HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = objectMapper.convertValue(userRegisterDTO, UserEntity.class);
        userRegisterRepository.save(userEntity);
        response.setSuccess(true);
        response.setMessage("User has been registered successfully");
        response.setDescription("User has been registered successfully. Please activate your account.");

        // Triggering Email to Customer
        emailService.sendEmail(userRegisterDTO);
        return response;

    }


}
