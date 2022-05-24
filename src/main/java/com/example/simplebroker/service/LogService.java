package com.example.simplebroker.service;

import java.util.List;

public interface LogService {
    void logIfNeeded(String message, String from, List<String> subscribers);
}
