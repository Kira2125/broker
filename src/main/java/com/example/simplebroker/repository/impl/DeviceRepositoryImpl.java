package com.example.simplebroker.repository.impl;

import com.example.simplebroker.enums.Status;
import com.example.simplebroker.exception.CustomException;
import com.example.simplebroker.model.Device;
import com.example.simplebroker.model.Message;
import com.example.simplebroker.repository.DeviceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    private final List<Device> devices = new CopyOnWriteArrayList<>();

    public void add(Device device) {
        devices.add(device);
    }

    public void removeByName(String deviceName) {
        devices.removeIf((el)-> Objects.equals(el.getName(), deviceName));
    }

    public List<Device> getAll() {
        return new CopyOnWriteArrayList<>(devices);
    }

    public Device getByName(String name) {
        return devices
                .stream()
                .filter((device -> Objects.equals(device.getName(), name)))
                .findFirst()
                .orElseThrow(() -> new CustomException("No such device registered"));
    }

    @Override
    public void changeMessagesStatusToPendingByName(String deviceName) {
        getByName(deviceName).getMessageQueue().forEach(Message::changeMessageStatusToPending);
    }

    @Override
    public void deletePendingMessagesByName(String deviceName) {
        getByName(deviceName).getMessageQueue().removeIf(next -> Objects.equals(next.getStatus(), Status.PENDING));
    }
}
