package com.example.simplebroker.repository;

import com.example.simplebroker.model.entities.MessageDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageDeviceRepository extends JpaRepository<MessageDevice, UUID> {
}
