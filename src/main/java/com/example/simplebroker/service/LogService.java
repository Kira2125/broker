package com.example.simplebroker.service;

import java.util.List;

public interface LogService {
    void checkForLogging(String message, String from, List<String> subscribers);
}
