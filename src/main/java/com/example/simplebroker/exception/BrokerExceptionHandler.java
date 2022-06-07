package com.example.simplebroker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BrokerExceptionHandler {

    private final String DEFAULT_MESSAGE = "Inner service error";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorRsDto> handleBrokerException(CustomException customException) {
        log.error("custom exception", customException);
        return new ResponseEntity<>(new ErrorRsDto(customException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorRsDto> handleServletBindingException(ServletRequestBindingException bindingException) {
        log.error("servlet binding exception", bindingException);
        return new ResponseEntity<>(new ErrorRsDto(bindingException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRsDto> handleUnexpectedException(Exception e) {
        log.error("unexpected exception", e);
        return new ResponseEntity<>(new ErrorRsDto(DEFAULT_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
