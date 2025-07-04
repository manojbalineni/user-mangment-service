package com.online.busbooking.user_mangment_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.busbooking.user_mangment_service.constants.CommonConstants;
import com.online.busbooking.user_mangment_service.entity.LoginAttemptEntity;
import com.online.busbooking.user_mangment_service.entity.UserEntity;
import com.online.busbooking.user_mangment_service.exception.*;
import com.online.busbooking.user_mangment_service.model.LoginRequestDTO;
import com.online.busbooking.user_mangment_service.model.LoginResponseDTO;
import com.online.busbooking.user_mangment_service.model.SuccessResponseDTO;
import com.online.busbooking.user_mangment_service.model.UserRegisterDTO;
import com.online.busbooking.user_mangment_service.repository.LoginAttemptRepository;
import com.online.busbooking.user_mangment_service.repository.UserRegisterRepository;
import com.online.busbooking.user_mangment_service.service.UserRegistrationService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    private final UserRegisterRepository userRegisterRepository;
    private final ObjectMapper objectMapper;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptRepository loginAttemptRepository;


    public UserRegistrationServiceImpl(UserRegisterRepository userRegisterRepository ,
                                       ObjectMapper objectMapper,
                                       EmailServiceImpl emailService,
                                       PasswordEncoder passwordEncoder,
                                       LoginAttemptRepository loginAttemptRepository)
                                       {
        this.userRegisterRepository = userRegisterRepository;
        this.objectMapper = objectMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptRepository = loginAttemptRepository;

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
        userEntity.setActive(false);
        String token = UUID.randomUUID().toString();
        userEntity.setToken(token);
        userRegisterRepository.save(userEntity);
        response.setSuccess(true);
        response.setMessage("User has been registered successfully");
        response.setDescription("User has been registered successfully. Please activate your account.");
        // Triggering Email to Customer

        emailService.sendEmail(userRegisterDTO,token);
        return response;
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
      UserEntity userEntity = userRegisterRepository.findByEmail(loginRequestDTO.getEmailId());
        if (userEntity == null) {
            throw new UserNotFoundException("US003" , "User Not Found" , HttpStatus.BAD_REQUEST);
        }
        if(!userEntity.isActive()){
            throw new GenericException("US006" , "Account is not activated. Please activate it." , HttpStatus.BAD_REQUEST);
        }
        LoginAttemptEntity loginAttempt = loginAttemptRepository.findByEmailId(loginRequestDTO.getEmailId())
                .orElse(new LoginAttemptEntity());

        if(loginAttempt.getEmailId() == null){
            loginAttempt.setEmailId(loginRequestDTO.getEmailId());
            loginAttempt.setFailedAttempts(0);
            loginAttempt.setAccountLocked(false);
        }
        if(loginAttempt.isAccountLocked()){
            if(isLockTimeExpired(loginAttempt)){
                loginAttempt.setAccountLocked(Boolean.FALSE);
                loginAttempt.setFailedAttempts(0);
                loginAttempt.setLockedTime(null);
            }
            else{
                throw new AccountLockException("US004" , "Account Locked. Please try again after 15 minutes",HttpStatus.BAD_REQUEST);
            }
        }
      if(!passwordEncoder.matches(loginRequestDTO.getPassword() , userEntity.getPassword())){
          int newFailedAttempts = loginAttempt.getFailedAttempts() + 1;
          loginAttempt.setFailedAttempts(newFailedAttempts);
          loginAttempt.setLastModifiedTime(LocalDateTime.now());
          if(newFailedAttempts >= CommonConstants.MAX_ATTEMPTS){
              loginAttempt.setLockedTime(LocalDateTime.now());
              loginAttempt.setAccountLocked(Boolean.TRUE);
          }
          loginAttemptRepository.save(loginAttempt);
          throw new InvalidCredentialsException("US002" , "Invalid Password" , HttpStatus.BAD_REQUEST);
      }
      loginAttempt.setLockedTime(null);
      loginAttempt.setFailedAttempts(0);
      loginAttempt.setAccountLocked(Boolean.FALSE);
      loginAttempt.setLastModifiedTime(LocalDateTime.now());
      loginAttemptRepository.save(loginAttempt);
      LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
      loginResponseDTO.setSuccess(true);
      loginResponseDTO.setMessage("Profile Details fetched successfully");
      loginResponseDTO.setName(userEntity.getName());
      loginResponseDTO.setEmail(userEntity.getEmail());
      loginResponseDTO.setMobileNumber(userEntity.getMobileNumber());
      return loginResponseDTO;

    }

    @Override
    public SuccessResponseDTO activateAccount(String token) {
        UserEntity userEntity = userRegisterRepository.findByToken(token);
        if(userEntity == null){
            throw new GenericException("US005" , "Activation Token is not valid" , HttpStatus.BAD_REQUEST);
        }
        userEntity.setActive(true);
        userEntity.setToken(null);
        userRegisterRepository.save(userEntity);
        SuccessResponseDTO response = new SuccessResponseDTO();
        response.setSuccess(true);
        response.setMessage("Account activated Successfully.");
        response.setDescription("Please proceed for Login");
        return response;
    }

    private boolean isLockTimeExpired(LoginAttemptEntity loginAttempt) {
        LocalDateTime lockTime = loginAttempt.getLockedTime();
        return lockTime != null && LocalDateTime.now().isAfter(lockTime.plusMinutes(CommonConstants.MAX_TIME_MINUTES));
    }

}
