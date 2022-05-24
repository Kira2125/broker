package com.example.simplebroker.repository;

import com.example.simplebroker.model.Device;

import java.util.List;


public interface DeviceRepository {

    void add(Device device);

    void removeByName(String deviceName);

    List<Device> getAll();

    Device getByName(String name);
}
