package com.online.busbooking.user_mangment_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.busbooking.user_mangment_service.entity.UserEntity;
import com.online.busbooking.user_mangment_service.exception.InvalidCredentialsException;
import com.online.busbooking.user_mangment_service.exception.UserAlreadyExistsException;
import com.online.busbooking.user_mangment_service.exception.UserNotFoundException;
import com.online.busbooking.user_mangment_service.model.LoginRequestDTO;
import com.online.busbooking.user_mangment_service.model.LoginResponseDTO;
import com.online.busbooking.user_mangment_service.model.SuccessResponseDTO;
import com.online.busbooking.user_mangment_service.model.UserRegisterDTO;
import com.online.busbooking.user_mangment_service.repository.UserRegisterRepository;
import com.online.busbooking.user_mangment_service.service.UserRegistrationService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    private final UserRegisterRepository userRegisterRepository;
    private final ObjectMapper objectMapper;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;


    public UserRegistrationServiceImpl(UserRegisterRepository userRegisterRepository ,
                                       ObjectMapper objectMapper,
                                       EmailServiceImpl emailService,
                                       PasswordEncoder passwordEncoder)
                                       {
        this.userRegisterRepository = userRegisterRepository;
        this.objectMapper = objectMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;

    }


    @Override
    public SuccessResponseDTO registerUserDetails(UserRegisterDTO userRegisterDTO) throws MessagingException {
        SuccessResponseDTO response = new SuccessResponseDTO();
        UserEntity userDetails = userRegisterRepository.findByEmail(userRegisterDTO.getEmail());
        if(userDetails != null){
            throw new UserAlreadyExistsException("US001" , "user with the email already exists" , HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = objectMapper.convertValue(userRegisterDTO, UserEntity.class);
        String encryptedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        userEntity.setPassword(encryptedPassword);
        userRegisterRepository.save(userEntity);
        response.setSuccess(true);
        response.setMessage("User has been registered successfully");
        response.setDescription("User has been registered successfully. Please activate your account.");
        // Triggering Email to Customer
        emailService.sendEmail(userRegisterDTO);
        return response;
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
      UserEntity userEntity = userRegisterRepository.findByEmail(loginRequestDTO.getEmailId());
        if (userEntity == null) {
            throw new UserNotFoundException("US003" , "User Not Found" , HttpStatus.BAD_REQUEST);
        }
      if(!passwordEncoder.matches(loginRequestDTO.getPassword() , userEntity.getPassword())){
          throw new InvalidCredentialsException("US002" , "Invalid Password" , HttpStatus.BAD_REQUEST);
      }
      LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
      loginResponseDTO.setSuccess(true);
      loginResponseDTO.setMessage("Profile Details fetched successfully");
      loginResponseDTO.setName(userEntity.getName());
      loginResponseDTO.setEmail(userEntity.getEmail());
      loginResponseDTO.setMobileNumber(userEntity.getMobileNumber());
      return loginResponseDTO;

    }


}
