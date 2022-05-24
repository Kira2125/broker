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
    public void should_save_data_to_log() {
        String logMessage = logList.toString().replaceAll(", |\\[|]", " ");
        logService.logIfNeeded(logMessage, fromDeviceName, subscribers);
        Mockito.verify(logRepository, Mockito.timeout(1000).times(1)).save(Mockito.any());
    }

    @Test
    public void should_not_save_data_to_log() {
        logService.logIfNeeded(noLogMessage, fromDeviceName, subscribers);
        Mockito.verify(logRepository, Mockito.times(0)).save(Mockito.any());
    }
}
