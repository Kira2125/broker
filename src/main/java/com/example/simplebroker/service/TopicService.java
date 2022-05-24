package com.example.simplebroker.service;

import com.example.simplebroker.dto.rq.TopicRqDto;
import com.example.simplebroker.dto.rq.TopicSubscribeRqDto;
import com.example.simplebroker.dto.rs.TopicRsDto;

import java.util.List;

public interface TopicService {
    void create(TopicRqDto topicRqDto);
    List<TopicRsDto> getAllTopics();
    void subscribe(TopicSubscribeRqDto topicSubscribeRqDto, String deviceName);
}
