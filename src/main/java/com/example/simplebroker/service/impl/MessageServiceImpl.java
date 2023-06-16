package com.example.simplebroker.service.impl;

import com.example.simplebroker.dto.rq.SendMessageBroadcastRqDto;
import com.example.simplebroker.dto.rq.SendMessageDeviceRqDto;
import com.example.simplebroker.dto.rq.SendMessageTopicRqDto;
import com.example.simplebroker.dto.rs.MessageDto;
import com.example.simplebroker.dto.rs.MessagesRsDto;
import com.example.simplebroker.enums.Status;
import com.example.simplebroker.enums.TopicType;
import com.example.simplebroker.exception.ExceptionFactory;
import com.example.simplebroker.model.entities.Device;
import com.example.simplebroker.model.entities.Message;
import com.example.simplebroker.model.entities.MessageDevice;
import com.example.simplebroker.model.entities.Topic;
import com.example.simplebroker.repository.DeviceRepository;
import com.example.simplebroker.repository.MessageDeviceRepository;
import com.example.simplebroker.repository.MessageRepository;
import com.example.simplebroker.repository.TopicRepository;
import com.example.simplebroker.service.LogService;
import com.example.simplebroker.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.simplebroker.utils.Utils.PRIVATE_CHANNEL;

//@TODO move all sendMessage methods to topicService

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final DeviceRepository deviceRepository;
    private final TopicRepository topicRepository;
    private final LogService logService;
    private final MessageRepository messageRepository;
    private final MessageDeviceRepository messageDeviceRepository;

    @Override
    @Transactional
    public void sendMessagePrivate(SendMessageDeviceRqDto sendMessageDeviceRqDto, String deviceName) {
        Device toDevice = deviceRepository.findByName(sendMessageDeviceRqDto.getToDevice())
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Device.class, sendMessageDeviceRqDto.getToDevice()));
        final Topic privateTopic = topicRepository.getByNameAndType(PRIVATE_CHANNEL + toDevice.getId(), TopicType.PRIVATE)
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Topic.class, PRIVATE_CHANNEL + toDevice.getId()));

        Message message = Message.builder()
                .sourceDeviceName(deviceName)
                .content(sendMessageDeviceRqDto.getMessage())
                .build();

        MessageDevice messageDevice = MessageDevice.builder()
                .device(toDevice)
                .topic(privateTopic)
                .message(message)
                .build();

        messageDeviceRepository.save(messageDevice);

        message.setMessageDevices(new ArrayList<>(Collections.singletonList(messageDevice)));
        messageRepository.save(message);

        privateTopic.getMessageDevices().add(messageDevice);
        topicRepository.save(privateTopic);

        logService.logIfNeeded(message, deviceName,
                privateTopic.getId());
    }

    @Override
    @Transactional
    public void sendMessageTopic(SendMessageTopicRqDto sendMessageTopicRqDto, String deviceName) {
        Topic topic = topicRepository.getByName(sendMessageTopicRqDto.getTopic())
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Topic.class, sendMessageTopicRqDto.getTopic()));

        Message message = Message.builder()
                .sourceDeviceName(deviceName)
                .content(sendMessageTopicRqDto.getMessage())
                .build();

        List<Device> devices = topic.getDevices();
        List<MessageDevice> messageDevices = new ArrayList<>();

        for(Device d : devices) {
            MessageDevice messageDevice = MessageDevice.builder()
                    .device(d)
                    .topic(topic)
                    .message(message)
                    .build();
            messageDevices.add(messageDevice);
        }
        messageDeviceRepository.saveAll(messageDevices);

        message.setMessageDevices(messageDevices);
        messageRepository.save(message);

        topic.getMessageDevices().addAll(messageDevices);

        topicRepository.save(topic);

        logService.logIfNeeded(message, deviceName, topic.getId());
    }

    @Override
    @Transactional
    public void sendMessageBroadcast(SendMessageBroadcastRqDto sendMessageBroadcastRqDto, String deviceName) {

        Topic topic = topicRepository.getByType(TopicType.BROADCAST).stream()
                .findFirst()
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Topic.class, TopicType.BROADCAST.name()));

        Message message = Message.builder()
                .sourceDeviceName(deviceName)
                .content(sendMessageBroadcastRqDto.getMessage())
                .build();

        List<Device> devices = topic.getDevices();
        List<MessageDevice> messageDevices = new ArrayList<>();

        for(Device d : devices) {
            MessageDevice messageDevice = MessageDevice.builder()
                    .device(d)
                    .topic(topic)
                    .message(message)
                    .build();
            messageDevices.add(messageDevice);
        }

        messageDeviceRepository.saveAll(messageDevices);

        message.setMessageDevices(messageDevices);
        messageRepository.save(message);

        topic.getMessageDevices().addAll(messageDevices);
        topicRepository.save(topic);

//        logService.logIfNeeded(message, deviceName, topic.getId());
    }

    //@TODO return batch of messages to avoid subscriber overload
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessagesRsDto getMessages(String deviceName) {
        MessagesRsDto response = new MessagesRsDto();

        Device device = deviceRepository.findByName(deviceName)
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Device.class, deviceName));


        List<MessageDevice> messageDevices = device.getTopics()
                .stream()
                .map(Topic::getMessageDevices)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(messageDevice -> Objects.equals(messageDevice.getDevice().getName(), device.getName())
                        && (Objects.equals(messageDevice.getStatus(), Status.STABLE)
                        || Objects.equals(messageDevice.getStatus(), Status.PENDING)))
                .collect(Collectors.toList());

        List<Message> messageModels = messageDevices.stream()
                .map(MessageDevice::getMessage)
                .collect(Collectors.toList());

        final List<MessageDto> messages = messageModels.stream()
                .map(this::mapMessageToDto)
                .sorted(Comparator.comparing(MessageDto::getDate).reversed())
                .collect(Collectors.toList());

        for (MessageDevice messageDevice : messageDevices) {
            messageDevice.setStatus(Status.PENDING);
        }

        messageDeviceRepository.saveAll(messageDevices);
        response.setMessages(messages);

        return response;
    }

    @Override
    @Transactional
    public void acknowledgeMessages(String deviceName) {
        messageRepository.modifyStatusByDeviceName(deviceName, Status.DELETED.name());
    }

    private MessageDto mapMessageToDto(Message message) {
        return MessageDto
                .builder()
                .message(message.getContent())
                .senderDeviceName(message.getSourceDeviceName())
                .topic(message.getMessageDevices()
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> ExceptionFactory.getNotFoundException(MessageDevice.class))
                        .getTopic().getName())
                .date(message.getCreated())
                .build();
    }

}
