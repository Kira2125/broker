package com.example.simplebroker.controller;

import com.example.simplebroker.AbstractSpringBootTest;
import com.example.simplebroker.dto.rq.SendMessageBroadcastRqDto;
import com.example.simplebroker.dto.rq.SendMessageDeviceRqDto;
import com.example.simplebroker.dto.rq.SendMessageTopicRqDto;
import com.example.simplebroker.dto.rs.MessagesRsDto;
import com.example.simplebroker.service.MessageService;
import com.fasterxml.jackson.databind.DeserializationFeature;
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

public class MessageControllerTest extends AbstractSpringBootTest {

    private final String MESSAGE_URL = "/broker/message";
    private final String BROADCAST_URL = MESSAGE_URL + "/broadcast";
    private final String TOPIC_URL = MESSAGE_URL + "/topic";

    @LocalServerPort
    private int port;

    @MockBean
    private MessageService messageService;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
    }

    @Test
    public void should_send_message_to_broadcast() throws IOException {
        var rq = objectMapper
                .readValue(getPath("json/send_message_broadcast_rq_200.json"), SendMessageBroadcastRqDto.class);

        RestAssured
                .given()
                .headers(HEADERS)
                .contentType(ContentType.JSON)
                .body(rq)
                .when()
                .post(BROADCAST_URL)
                .then()
                .statusCode(200);

        verify(messageService, times(1)).sendMessageBroadcast(rq, DEVICE_NAME);
    }

    @Test
    public void should_send_message_to_device() throws IOException {
        var rq = objectMapper
                .readValue(getPath("json/send_message_device_rq_200.json"), SendMessageDeviceRqDto.class);

        RestAssured
                .given()
                .headers(HEADERS)
                .contentType(ContentType.JSON)
                .body(rq)
                .when()
                .post(MESSAGE_URL)
                .then()
                .statusCode(200);

        verify(messageService, times(1)).sendMessageDevice(rq, DEVICE_NAME);
    }

    @Test
    public void should_send_message_to_topic() throws IOException {
        var rq = objectMapper
                .readValue(getPath("json/send_message_topic_rq_200.json"), SendMessageTopicRqDto.class);

        RestAssured
                .given()
                .headers(HEADERS)
                .contentType(ContentType.JSON)
                .body(rq)
                .when()
                .post(TOPIC_URL)
                .then()
                .statusCode(200);

        verify(messageService, times(1)).sendMessageTopic(rq, DEVICE_NAME);
    }

    @Test
    public void should_get_all_messages() throws IOException {
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        var expected = objectMapper
                .readValue(getPath("json/all_messages_rs_200.json"), MessagesRsDto.class);
        when(messageService.getMessages(DEVICE_NAME)).thenReturn(expected);

        RestAssured
                .given()
                .headers(HEADERS)
                .when()
                .get(MESSAGE_URL)
                .then()
                .body(Matchers.equalTo(objectMapper.writeValueAsString(expected)))
                .statusCode(200);

        verify(messageService, times(1)).getMessages(DEVICE_NAME);
    }
}
