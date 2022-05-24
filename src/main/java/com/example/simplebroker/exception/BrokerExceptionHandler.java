package com.example.simplebroker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BrokerExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorRsDto> handleTelegramException(CustomException customException) {
        log.error("custom exception", customException);
        ErrorRsDto rs = new ErrorRsDto(customException.getMessage());
        return new ResponseEntity<>(rs, HttpStatus.BAD_REQUEST);
    }
}
