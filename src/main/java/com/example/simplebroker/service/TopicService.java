package com.example.simplebroker.service;

import com.example.simplebroker.dto.rq.TopicRqDto;
import com.example.simplebroker.dto.rq.TopicSubscribeRqDto;
import com.example.simplebroker.dto.rs.TopicDto;
import com.example.simplebroker.dto.rs.TopicsRsDto;

import java.util.List;

public interface TopicService {
    void create(TopicRqDto topicRqDto);
    TopicsRsDto getAllTopics();
    void subscribe(TopicSubscribeRqDto topicSubscribeRqDto, String deviceName);
}
