package com.example.simplebroker.service.impl;

import com.example.simplebroker.dto.rq.DeviceRqDto;
import com.example.simplebroker.dto.rs.DeviceDto;
import com.example.simplebroker.dto.rs.DevicesRsDto;
import com.example.simplebroker.enums.TopicType;
import com.example.simplebroker.exception.ExceptionFactory;
import com.example.simplebroker.model.entities.Device;
import com.example.simplebroker.model.entities.Topic;
import com.example.simplebroker.repository.DeviceRepository;
import com.example.simplebroker.repository.TopicRepository;
import com.example.simplebroker.service.DeviceService;
import com.example.simplebroker.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.simplebroker.utils.Utils.BROADCAST_CHANNEL;
import static com.example.simplebroker.utils.Utils.PRIVATE_CHANNEL;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final TopicRepository topicRepository;

    //@TODO device.getId() is null here, make a logic through device-topic reference instead of name
    @Override
    @Transactional
    public void registerDevice(DeviceRqDto deviceRqDto) {
        Device device = Device
                .builder()
                .name(deviceRqDto.getName())
                .build();
        Device savedDevice = deviceRepository.saveAndFlush(device);

        Topic privateTopic = Topic
                .builder()
                .name(PRIVATE_CHANNEL + savedDevice.getId())
                .type(TopicType.PRIVATE)
                .devices(new ArrayList<>(List.of(savedDevice)))
                .build();

        //@TODO create partial index on topic type
        //@TODO decide how to organize broadcasting and public topics with deletion after reading, we cannot create copy for every broadcast and public topic
        Topic broadcastTopic = Topic
                .builder()
                .name(BROADCAST_CHANNEL + savedDevice.getId())
                .type(TopicType.BROADCAST)
                .devices(new ArrayList<>(List.of(savedDevice)))
                .build();
        broadcastTopic.getDevices().add(device);
        topicRepository.saveAll(new ArrayList<>(List.of(privateTopic, broadcastTopic)));

        savedDevice.setTopics(new ArrayList<>(List.of(privateTopic, broadcastTopic)));
        deviceRepository.save(savedDevice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deregisterDevice(String deviceName) {
        Device device = deviceRepository.findByName(deviceName)
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Device.class, deviceName));

        Topic privateTopic = device.getTopics().stream()
                .filter(t-> Objects.equals(t.getType(), TopicType.PRIVATE))
                .findFirst()
                .orElseThrow(() -> ExceptionFactory.getNotFoundException(Topic.class, PRIVATE_CHANNEL + device.getId()));

        deviceRepository.delete(device);
        topicRepository.delete(privateTopic);
    }


//    @TODO - pagination
    @Override
    public DevicesRsDto getAllDevices() {
        List<DeviceDto> devices = deviceRepository.findAll()
                .stream()
                .map(device -> DeviceDto
                        .builder()
                        .name(device.getName())
                        .build())
                .collect(Collectors.toList());
        return new DevicesRsDto(devices);
    }
}
