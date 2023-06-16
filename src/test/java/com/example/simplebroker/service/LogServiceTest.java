package com.example.simplebroker.service;

import com.example.simplebroker.AbstractSpringBootTest;
import com.example.simplebroker.model.entities.Message;
import com.example.simplebroker.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

public class LogServiceTest extends AbstractSpringBootTest {

    @Autowired
    private LogService logService;

    @MockBean
    private LogRepository logRepository;

    @Value("#{'${broker.log}'.split(',')}")
    private List<String> logList;

    private final String noLogMessage = "test4wdddda";
    private final String fromDeviceName = "destination";

    @Test
    public void should_save_data_to_log() {
        String logMessage = logList.toString().replaceAll(", |\\[|]", " ");
        Message message = Message.builder()
                .id(UUID.randomUUID())
                .content(logMessage)
                .build();

        logService.logIfNeeded(message, fromDeviceName, UUID.randomUUID());
        Mockito.verify(logRepository, Mockito.timeout(1000).times(1)).save(Mockito.any());
    }

    @Test
    public void should_not_save_data_to_log() {
        Message message = Message.builder()
                .id(UUID.randomUUID())
                .content(noLogMessage)
                .build();

        logService.logIfNeeded(message, fromDeviceName, UUID.randomUUID());
        Mockito.verify(logRepository, Mockito.timeout(1000).times(0)).save(Mockito.any());
    }
}
