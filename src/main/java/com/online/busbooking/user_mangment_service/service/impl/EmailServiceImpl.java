package com.online.busbooking.user_mangment_service.service.impl;


import com.online.busbooking.user_mangment_service.entity.UserEntity;
import com.online.busbooking.user_mangment_service.model.UserRegisterDTO;
import com.online.busbooking.user_mangment_service.repository.UserRegisterRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final UserRegisterRepository userRegisterRepository;

    public EmailServiceImpl(TemplateEngine templateEngine , JavaMailSender javaMailSender,
                            UserRegisterRepository userRegisterRepository){
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.userRegisterRepository = userRegisterRepository;
    }


    @Async
    public void sendEmail(UserRegisterDTO userRegisterDTO , String token) throws MessagingException {
        Context context = new Context();
        context.setVariable("username" , userRegisterDTO.getName());
        String activationLink = "http://localhost:8081/user/activate?token="+token;
        context.setVariable("activationLink",activationLink);
        String htmlContent = templateEngine.process("activation-email" , context);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        mimeMessageHelper.setTo(userRegisterDTO.getEmail());
        mimeMessageHelper.setText(htmlContent , true);
        mimeMessageHelper.setSubject("Account Activation");
        javaMailSender.send(message);
        UserEntity userEntity = userRegisterRepository.findByEmail(userRegisterDTO.getEmail());
        userEntity.setRegisterEmail(1);
        userRegisterRepository.save(userEntity);
    }
}
