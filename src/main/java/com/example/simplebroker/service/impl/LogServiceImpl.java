package com.example.simplebroker.service.impl;

import com.example.simplebroker.model.Log;
import com.example.simplebroker.repository.LogRepository;
import com.example.simplebroker.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    @Value("#{'${broker.log}'.split(',')}")
    private List<String> logList;

    private final LogRepository logRepository;

    @Override
    @Async
    public void checkForLogging(String message, String from, List<String> subscribers) {
        List<String> keywords = logList.stream()
                .filter(message::contains)
                .collect(Collectors.toList());
        logRepository.save(Log
                .builder()
                .message(message)
                .keywords(keywords)
                .recipients(subscribers)
                .sender(from)
                .build());

    }
}
