package com.example.simplebroker.service;

import com.example.simplebroker.dto.rq.SendMessageTopicRqDto;
import com.example.simplebroker.dto.rq.SendMessageBroadcastRqDto;
import com.example.simplebroker.dto.rq.SendMessageDeviceRqDto;
import com.example.simplebroker.dto.rs.MessageDto;
import com.example.simplebroker.dto.rs.MessagesRsDto;

import java.util.concurrent.LinkedBlockingQueue;

public interface MessageService {
    void sendMessageTopic(SendMessageTopicRqDto sendMessageTopicRqDto, String deviceName);
    void sendMessageBroadcast(SendMessageBroadcastRqDto sendMessageBroadcastRqDto, String deviceName);
    void sendMessagePrivate(SendMessageDeviceRqDto sendMessageDeviceRqDto, String deviceName);
    MessagesRsDto getMessages(String deviceName);
    void acknowledgeMessages(String deviceName);
}
