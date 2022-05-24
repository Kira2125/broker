package com.example.simplebroker.service.impl;

import com.example.simplebroker.dto.rq.DeviceRqDto;
import com.example.simplebroker.dto.rs.DeviceDto;
import com.example.simplebroker.dto.rs.DevicesRsDto;
import com.example.simplebroker.model.Device;
import com.example.simplebroker.repository.DeviceRepository;
import com.example.simplebroker.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    public void registerDevice(DeviceRqDto deviceRqDto) {
        deviceRepository.add(new Device(deviceRqDto.getName()));
    }

    @Override
    public void deregisterDevice(String deviceName) {
        deviceRepository.removeByName(deviceName);
    }

    @Override
    public DevicesRsDto getAllDevices() {
        List<DeviceDto> devices = deviceRepository.getAll()
                .stream()
                .map(device -> DeviceDto
                        .builder()
                        .name(device.getName())
                        .build())
                .collect(Collectors.toList());
        return new DevicesRsDto(devices);
    }
}
