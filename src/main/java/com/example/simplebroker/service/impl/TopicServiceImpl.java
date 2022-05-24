package com.example.simplebroker.service.impl;

import com.example.simplebroker.dto.rs.TopicsRsDto;
import com.example.simplebroker.exception.CustomException;
import com.example.simplebroker.dto.rq.TopicRqDto;
import com.example.simplebroker.dto.rq.TopicSubscribeRqDto;
import com.example.simplebroker.dto.rs.TopicDto;
import com.example.simplebroker.model.Device;
import com.example.simplebroker.repository.DeviceRepository;
import com.example.simplebroker.repository.TopicRepository;
import com.example.simplebroker.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public void create(TopicRqDto topicRqDto) {
        topicRepository.add(topicRqDto.getName());
    }

    @Override
    public TopicsRsDto getAllTopics() {
        List<TopicDto> topics = topicRepository.getAll()
                .stream()
                .map(TopicDto::new)
                .collect(Collectors.toList());
        return new TopicsRsDto(topics);
    }

    @Override
    public void subscribe(TopicSubscribeRqDto topicSubscribeRqDto, String deviceName) {
        Device device = deviceRepository.getByName(deviceName);
        if(!topicRepository.exist(topicSubscribeRqDto.getName())) {
            throw new CustomException("No such topic exists");
        }
        device.getTopics()
                .add(topicSubscribeRqDto.getName());
    }
}
