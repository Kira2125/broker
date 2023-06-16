package com.example.simplebroker.repository;

import com.example.simplebroker.model.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface DeviceRepository extends JpaRepository<Device, UUID> {

    void deleteByName(String deviceName);

    Optional<Device> findByName(String deviceName);


//    Device getByName(String name);

//    void changeMessagesStatusToPendingByName(String deviceName);
//
//    void deletePendingMessagesByName(String deviceName);
}
