package com.example.simplebroker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Log {
    private final String sender;
    private final List<String> keywords;
    private final String message;
    private final List<String> recipients;
}
