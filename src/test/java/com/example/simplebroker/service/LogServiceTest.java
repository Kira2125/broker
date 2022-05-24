package com.example.simplebroker.service;

import com.example.simplebroker.AbstractSpringBootTest;
import com.example.simplebroker.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

public class LogServiceTest extends AbstractSpringBootTest {

    @Autowired
    private LogService logService;

    @MockBean
    private LogRepository logRepository;

    @Value("#{'${broker.log}'.split(',')}")
    private List<String> logList;

    private final String noLogMessage = "test4wdddda";
    private final String fromDeviceName = "destination";
    private final List<String> subscribers = List.of("lenovo22", "imac4");

    @Test
    void should_save_data_to_log() {
        String logMessage = logList.toString().replaceAll(", |\\[|]", " ");
        logService.checkForLogging(logMessage, fromDeviceName, subscribers);
        Mockito.verify(logRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void should_not_save_data_to_log() {
        logService.checkForLogging(noLogMessage, fromDeviceName, subscribers);
        Mockito.verify(logRepository, Mockito.times(0)).save(Mockito.any());
    }
}
