package com.online.busbooking.user_mangment_service.exception;


import com.online.busbooking.user_mangment_service.util.ResponseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    private final ResponseHandler responseHandler;
    public GlobalExceptionHandler(ResponseHandler responseHandler){
        this.responseHandler = responseHandler;
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException exception){
        Object error =  responseHandler.generateErrorMessage(exception.getCode() , exception.getMessage(),exception.getHttpStatus());
        return new ResponseEntity<>(error , exception.getHttpStatus());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentailsException(InvalidCredentialsException exception){
        Object error = responseHandler.generateErrorMessage(exception.getCode() , exception.getMessage() , exception.getHttpStatus());
        return new ResponseEntity<>(error , exception.getHttpStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception){
        Object error = responseHandler.generateErrorMessage(exception.getCode() , exception.getMessage() , exception.getHttpStatus());
        return new ResponseEntity<>(error , exception.getHttpStatus());
    }

}
