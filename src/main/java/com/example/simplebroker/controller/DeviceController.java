package com.example.simplebroker.controller;

import com.example.simplebroker.dto.rq.DeviceRqDto;
import com.example.simplebroker.dto.rs.DevicesRsDto;
import com.example.simplebroker.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Сontroller for registering devices/subscribers in broker
 */

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * Register new device/subscriber in broker
     * @param deviceRqDto - device info
     */

    @PostMapping
    public void registerDevice(@RequestBody DeviceRqDto deviceRqDto) {
        deviceService.registerDevice(deviceRqDto);
    }

    /**
     * Remove device/subscriber from broker
     * @param deviceName - device/subscriber name
     */
    @DeleteMapping
    public void deregisterDevice(@RequestHeader("X-DEVICE") String deviceName) {
        deviceService.deregisterDevice(deviceName);
    }

    /**
     * Get all devices/subscribers in broker
     */

    @GetMapping
    public DevicesRsDto allDevices() {
        return deviceService.getAllDevices();
    }
}
