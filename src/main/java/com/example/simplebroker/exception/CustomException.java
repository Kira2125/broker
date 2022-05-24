package com.example.simplebroker.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomException extends RuntimeException {
    private String message;
}
