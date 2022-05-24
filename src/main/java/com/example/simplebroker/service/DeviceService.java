package com.example.simplebroker.service;

import com.example.simplebroker.dto.rq.DeviceRqDto;
import com.example.simplebroker.dto.rs.DevicesRsDto;

public interface DeviceService {
    void registerDevice(DeviceRqDto deviceRqDto);
    void deregisterDevice(String deviceName);
    DevicesRsDto getAllDevices();
}
