package com.example.simplebroker.service;

import com.example.simplebroker.model.entities.Message;

import java.util.UUID;

public interface LogService {

    void logIfNeeded(Message message, String senderId, UUID topicId);
}
