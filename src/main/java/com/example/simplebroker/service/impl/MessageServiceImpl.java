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
import com.example.simplebroker.model.entities.Topic;
import com.example.simplebroker.repository.DeviceRepository;
import com.example.simplebroker.repository.MessageRepository;
import com.example.simplebroker.repository.TopicRepository;
import com.example.simplebroker.service.LogService;
import com.example.simplebroker.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
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

    @Override
    public void sendMessagePrivate(SendMessageDeviceRqDto sendMessageDeviceRqDto, String deviceName) {
        Device toDevice = deviceRepository.findByName(sendMessageDeviceRqDto.getToDevice())
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Device.class, sendMessageDeviceRqDto.getToDevice()));
        final Topic privateTopic = topicRepository.getByNameAndType(PRIVATE_CHANNEL + toDevice.getId(), TopicType.PRIVATE)
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Topic.class, PRIVATE_CHANNEL + toDevice.getId()));


        Message message = Message.builder()
                .sourceDeviceName(deviceName)
                .content(sendMessageDeviceRqDto.getMessage())
                .topic(privateTopic)
                .build();

        privateTopic.getMessages().add(message);
        topicRepository.save(privateTopic);

        logService.logIfNeeded(message, deviceName,
                privateTopic.getId());
    }

    @Override
    public void sendMessageTopic(SendMessageTopicRqDto sendMessageTopicRqDto, String deviceName) {
        Topic topic = topicRepository.getByName(sendMessageTopicRqDto.getTopic())
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Topic.class, sendMessageTopicRqDto.getTopic()));

        Message message = Message.builder()
                .sourceDeviceName(deviceName)
                .topic(topic)
                .content(sendMessageTopicRqDto.getMessage())
                .build();

        topic.getMessages().add(message);

        topicRepository.save(topic);

        logService.logIfNeeded(message, deviceName, topic.getId());
    }

    @Override
    public void sendMessageBroadcast(SendMessageBroadcastRqDto sendMessageBroadcastRqDto, String deviceName) {

        List<Topic> topics = topicRepository.getByType(TopicType.BROADCAST);

        for(Topic topic : topics) {
            Message message = Message.builder()
                    .topic(topic)
                    .sourceDeviceName(deviceName)
                    .content(sendMessageBroadcastRqDto.getMessage())
                    .build();

            topic.getMessages().add(message);
        }

        topicRepository.saveAll(topics);

//        logService.logIfNeeded(message, deviceName, topic.getId());
    }

    //@TODO return batch of messages to avoid subscriber overload
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessagesRsDto getMessages(String deviceName) {
        MessagesRsDto response = new MessagesRsDto();

        Device device = deviceRepository.findByName(deviceName)
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Device.class, deviceName));

        List<Topic> topics = device.getTopics();

        if (topics.isEmpty()) {
            response.setMessages(List.of());
            return response;
        }

        List<Message> messageModels = topics.stream()
                .flatMap(topic -> topic.getMessages().stream())
                .collect(Collectors.toList());

        final List<MessageDto> messages = messageModels.stream()
                .map(this::mapMessageToDto)
                .sorted(Comparator.comparing(MessageDto::getDate).reversed())
                .collect(Collectors.toList());

        for(Message m : messageModels) {
            m.setStatus(Status.PENDING);
        }

        messageRepository.saveAll(messageModels);
        response.setMessages(messages);

        return response;
    }

    @Override
    public void acknowledgeMessages(String deviceName) {
        messageRepository.deleteStatusPendingBySourceDeviceName(deviceName);
    }

    private MessageDto mapMessageToDto(Message message) {
        return MessageDto
                .builder()
                .message(message.getContent())
                .senderDeviceName(message.getSourceDeviceName())
                .topic(message.getTopic().getName())
                .date(message.getCreated())
                .build();
    }

}
