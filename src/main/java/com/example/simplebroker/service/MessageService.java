package com.example.simplebroker.service;

import com.example.simplebroker.dto.rq.SendMessageBroadcastRqDto;
import com.example.simplebroker.dto.rq.SendMessageDeviceRqDto;
import com.example.simplebroker.dto.rq.SendMessageTopicRqDto;
import com.example.simplebroker.dto.rs.MessagesRsDto;

public interface MessageService {
    void sendMessageTopic(SendMessageTopicRqDto sendMessageTopicRqDto, String deviceName);
    void sendMessageBroadcast(SendMessageBroadcastRqDto sendMessageBroadcastRqDto, String deviceName);
    void sendMessageDevice(SendMessageDeviceRqDto sendMessageDeviceRqDto, String deviceName);
    MessagesRsDto getMessages(String deviceName, int batchSize);
    void acknowledgeMessages(String deviceName, int batchSize);
}
