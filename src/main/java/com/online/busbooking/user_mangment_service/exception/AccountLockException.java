package com.online.busbooking.user_mangment_service.exception;

import org.springframework.http.HttpStatus;

public class AccountLockException extends RuntimeException{
    private String code;
    private String message;
    private HttpStatus httpStatus;

    public AccountLockException(String code , String message , HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
