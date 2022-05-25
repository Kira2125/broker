package com.example.simplebroker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BrokerExceptionHandler {

    private final String DEFAULT_MESSAGE = "Inner service error";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorRsDto> handleBrokerException(CustomException customException) {
        log.error("custom exception", customException);
        ErrorRsDto rs = new ErrorRsDto(customException.getMessage());
        return new ResponseEntity<>(rs, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRsDto> handleUnexpectedException(Exception e) {
        log.error("unexpected exception", e);
        ErrorRsDto rs = new ErrorRsDto(DEFAULT_MESSAGE);
        return new ResponseEntity<>(rs, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
