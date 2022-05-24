package com.example.simplebroker.service.impl;

import com.example.simplebroker.dto.rq.SendMessageBroadcastRqDto;
import com.example.simplebroker.dto.rq.SendMessageDeviceRqDto;
import com.example.simplebroker.dto.rq.SendMessageTopicRqDto;
import com.example.simplebroker.dto.rs.MessageRsDto;
import com.example.simplebroker.exception.CustomException;
import com.example.simplebroker.model.Device;
import com.example.simplebroker.model.Message;
import com.example.simplebroker.repository.DeviceRepository;
import com.example.simplebroker.repository.TopicRepository;
import com.example.simplebroker.service.LogService;
import com.example.simplebroker.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final DeviceRepository deviceRepository;
    private final TopicRepository topicRepository;
    private final LogService logService;

    @Override
    public void sendMessageDevice(SendMessageDeviceRqDto sendMessageDeviceRqDto, String deviceName) {
        Device toDevice = deviceRepository.getByName(sendMessageDeviceRqDto.getToDevice());
        toDevice.getMessageQueue().add(createMessage(sendMessageDeviceRqDto.getMessage(), deviceName));
        logService.checkForLogging(sendMessageDeviceRqDto.getMessage(), deviceName, List.of(sendMessageDeviceRqDto.getToDevice()));
    }

    @Override
    public void sendMessageTopic(SendMessageTopicRqDto sendMessageTopicRqDto, String deviceName) {
        if (!topicRepository.exist(sendMessageTopicRqDto.getTopic())) {
            throw new CustomException("No such topic exists");
        }
        List<Device> subscribers = getSubscribers(deviceName)
                .stream()
                .filter(device -> device.getTopics().contains(sendMessageTopicRqDto.getTopic()))
                .collect(Collectors.toList());
        subscribers.forEach(
                device -> device.getMessageQueue().add(createMessageWithTopic(sendMessageTopicRqDto.getMessage(),
                        deviceName, sendMessageTopicRqDto.getTopic())));
        logService.checkForLogging(sendMessageTopicRqDto.getMessage(), deviceName, getSubscribersNames(subscribers));
    }

    @Override
    public void sendMessageBroadcast(SendMessageBroadcastRqDto sendMessageBroadcastRqDto, String deviceName) {
        List<Device> subscribers = getSubscribers(deviceName);
        subscribers.forEach(
                device -> device.getMessageQueue().add(createMessage(sendMessageBroadcastRqDto.getMessage(), deviceName)));
        logService.checkForLogging(sendMessageBroadcastRqDto.getMessage(), deviceName, getSubscribersNames(subscribers));
    }

    @Override
    public LinkedBlockingQueue<MessageRsDto> getMessages(String deviceName) {
        Queue<Message> messageQueue = deviceRepository.getByName(deviceName).getMessageQueue();
        LinkedBlockingQueue<MessageRsDto> messageRsDtos = messageQueue
                .stream()
                .map(this::mapMessageToDto)
                .collect(Collectors.toCollection(LinkedBlockingQueue::new));
        messageQueue.clear();
        return messageRsDtos;
    }

    private List<Device> getSubscribers(String deviceName) {
        return deviceRepository.getAll()
                .stream()
                .filter(device -> !Objects.equals(device.getName(), deviceName))
                .collect(Collectors.toList());
    }

    private List<String> getSubscribersNames(List<Device> subscribers) {
        return subscribers.stream()
                .map(Device::getName)
                .collect(Collectors.toList());
    }

    private Message createMessageWithTopic(String message, String sender, String topic) {
        return create(message, sender)
                .topic(topic)
                .build();
    }

    private Message createMessage(String message, String sender) {
        return create(message, sender)
                .build();
    }

    private Message.MessageBuilder create(String message, String sender) {
        return Message
                .builder()
                .message(message)
                .senderDeviceName(sender)
                .date(new Timestamp(new Date().getTime()));
    }

    private MessageRsDto mapMessageToDto(Message message) {
        return MessageRsDto
                .builder()
                .message(message.getMessage())
                .senderDeviceName(message.getSenderDeviceName())
                .topic(message.getTopic())
                .date(message.getDate())
                .build();
    }

}
