package com.example.simplebroker.controller;

import com.example.simplebroker.AbstractSpringBootTest;
import com.example.simplebroker.dto.rq.TopicRqDto;
import com.example.simplebroker.dto.rq.TopicSubscribeRqDto;
import com.example.simplebroker.dto.rs.TopicsRsDto;
import com.example.simplebroker.service.TopicService;
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

public class TopicControllerTest extends AbstractSpringBootTest {

    private final String TOPIC_URL = "/broker/topic";
    private final String SUBSCRIBE_URL = TOPIC_URL + "/subscribe";

    @LocalServerPort
    private int port;

    @MockBean
    private TopicService topicService;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
    }

    @Test
    public void should_create_topic() throws IOException {
        var rq = objectMapper
                .readValue(getPath("json/create_topic_rq_200.json"), TopicRqDto.class);

        RestAssured
                .given()
                .headers(HEADERS)
                .contentType(ContentType.JSON)
                .body(rq)
                .when()
                .post(TOPIC_URL)
                .then()
                .statusCode(200);

        verify(topicService, times(1)).create(rq);
    }

    @Test
    public void should_get_all_topics() throws IOException {
        var expected = objectMapper
                .readValue(getPath("json/all_topics_rs_200.json"), TopicsRsDto.class);
        when(topicService.getAllTopics()).thenReturn(expected);

        RestAssured
                .given()
                .when()
                .get(TOPIC_URL)
                .then()
                .body(Matchers.equalTo(objectMapper.writeValueAsString(expected)))
                .statusCode(200);

        verify(topicService, times(1)).getAllTopics();
    }

    @Test
    public void should_subscribe_topic() throws IOException {
        var rq = objectMapper
                .readValue(getPath("json/subscribe_topic_rq_200.json"), TopicSubscribeRqDto.class);

        RestAssured
                .given()
                .headers(HEADERS)
                .contentType(ContentType.JSON)
                .body(rq)
                .when()
                .post(SUBSCRIBE_URL)
                .then()
                .statusCode(200);

        verify(topicService, times(1)).subscribe(rq, DEVICE_NAME);
    }
}
