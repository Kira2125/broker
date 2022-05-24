package com.example.simplebroker.controller;

import com.example.simplebroker.AbstractSpringBootTest;
import com.example.simplebroker.dto.rq.DeviceRqDto;
import com.example.simplebroker.dto.rs.DevicesRsDto;
import com.example.simplebroker.service.DeviceService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeviceControllerTest extends AbstractSpringBootTest {

    private final String DEVICE_URL = "/broker/device";

    @LocalServerPort
    private int port;

    @MockBean
    private DeviceService deviceService;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
    }

    @Test
    public void should_register_device() throws IOException {
        var rq = objectMapper.readValue(getPath("json/register_device_rq_200.json"), DeviceRqDto.class);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(rq)
                .when()
                .post(DEVICE_URL)
                .then()
                .statusCode(200);

        verify(deviceService, times(1)).registerDevice(rq);
    }

    @Test
    public void should_deregister_device() {
        RestAssured
                .given()
                .headers(HEADERS)
                .when()
                .delete(DEVICE_URL)
                .then()
                .statusCode(200);

        verify(deviceService, times(1)).deregisterDevice(DEVICE_NAME);
    }

    @Test
    public void should_get_all_devices() throws IOException {
        var expected = objectMapper.readValue(getPath("json/get_all_devices_rs_200.json"), DevicesRsDto.class);
        when(deviceService.getAllDevices()).thenReturn(expected);

        RestAssured
                .given()
                .when()
                .get(DEVICE_URL)
                .then()
                .body(Matchers.equalTo(objectMapper.writeValueAsString(expected)))
                .statusCode(200);

        verify(deviceService, times(1)).getAllDevices();
    }
}
