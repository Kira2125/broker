package com.example.simplebroker.service;

import com.example.simplebroker.dto.rq.DeviceRqDto;
import com.example.simplebroker.dto.rs.DeviceRsDto;

import java.util.List;

public interface DeviceService {
    void registerDevice(DeviceRqDto deviceRqDto);
    void deregisterDevice(String deviceName);
    List<DeviceRsDto> getAllDevices();
}
