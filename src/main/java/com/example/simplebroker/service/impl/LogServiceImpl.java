package com.example.simplebroker.service.impl;

import com.example.simplebroker.model.Log;
import com.example.simplebroker.model.entities.Message;
import com.example.simplebroker.repository.LogRepository;
import com.example.simplebroker.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    @Value("#{'${broker.log}'.split(',')}")
    private List<String> logList;

    private final LogRepository logRepository;

    @Override
    @Async
    public void logIfNeeded(Message message, String senderName, UUID topicId) {
        List<String> keywords = checkForKeywords(message.getContent());
        logIfNeeded(message.getId(), senderName, topicId, keywords);
    }

    private List<String> checkForKeywords(String message) {
        return logList.stream()
                .filter(message::contains)
                .collect(Collectors.toList());
    }

    private void logIfNeeded(UUID messageId, String deviceId, UUID topicId, List<String> keywords) {
        if(!keywords.isEmpty()) {
            logRepository.save(Log
                    .builder()
                    .messageId(messageId)
                    .topicId(topicId)
                    .senderName(deviceId)
                    .keywords(keywords)
                    .build());
        }
    }
}
