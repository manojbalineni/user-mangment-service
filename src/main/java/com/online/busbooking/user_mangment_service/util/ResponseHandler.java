package com.online.busbooking.user_mangment_service.util;

import com.online.busbooking.user_mangment_service.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseHandler {


    public Map<String,Object> generateErrorMessage(String code , String message , HttpStatus httpStatus) {
        HashMap<String,Object> error = new HashMap<>();
        error.put("timestamp" , getTimeStamp());
        error.put("errorCode" , code);
        error.put("errorMessage" , message);
        error.put("httpStatus" ,httpStatus.value());
        return error;
    }

    private String getTimeStamp() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return  dateTime.format(formatter);
    }
}
