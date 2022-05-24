package com.example.simplebroker.controller;

import com.example.simplebroker.dto.rq.DeviceRqDto;
import com.example.simplebroker.dto.rs.DeviceRsDto;
import com.example.simplebroker.model.Device;
import com.example.simplebroker.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping
    public void registerDevice(@RequestBody DeviceRqDto deviceRqDto) {
        deviceService.registerDevice(deviceRqDto);
    }

    @DeleteMapping
    public void deregisterDevice(@RequestHeader("X-DEVICE") String deviceName) {
        deviceService.deregisterDevice(deviceName);
    }

    @GetMapping
    public List<DeviceRsDto> allDevices() {
        return deviceService.getAllDevices();
    }
}
