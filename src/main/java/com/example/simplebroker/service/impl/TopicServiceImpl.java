package com.example.simplebroker.service.impl;

import com.example.simplebroker.dto.rs.TopicsRsDto;
import com.example.simplebroker.exception.CustomException;
import com.example.simplebroker.dto.rq.TopicRqDto;
import com.example.simplebroker.dto.rq.TopicSubscribeRqDto;
import com.example.simplebroker.dto.rs.TopicDto;
import com.example.simplebroker.exception.ExceptionFactory;
import com.example.simplebroker.model.entities.Device;
import com.example.simplebroker.model.entities.Topic;
import com.example.simplebroker.repository.DeviceRepository;
import com.example.simplebroker.repository.TopicRepository;
import com.example.simplebroker.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public void create(TopicRqDto topicRqDto) {
        topicRepository.save(Topic
                .builder()
                .name(topicRqDto.getName())
                .build());
    }

    @Override
    public TopicsRsDto getAllTopics() {
        List<TopicDto> topics = topicRepository.findAll()
                .stream()
                .map(topic -> TopicDto
                        .builder()
                        .name(topic.getName())
                        .build())
                .collect(Collectors.toList());
        return new TopicsRsDto(topics);
    }

    @Override
    public void subscribe(TopicSubscribeRqDto topicSubscribeRqDto, String deviceName) {
        Device device = deviceRepository.findByName(deviceName)
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Device.class, deviceName));
        Topic topic = topicRepository.getByName(topicSubscribeRqDto.getName())
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Topic.class, topicSubscribeRqDto.getName()));

        device.getTopics().add(topic);
        deviceRepository.save(device);

        topic.getDevices().add(device);
        topicRepository.save(topic);
    }
}
