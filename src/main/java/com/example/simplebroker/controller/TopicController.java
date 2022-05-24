package com.example.simplebroker.controller;

import com.example.simplebroker.dto.rq.TopicRqDto;
import com.example.simplebroker.dto.rq.TopicSubscribeRqDto;
import com.example.simplebroker.dto.rs.TopicsRsDto;
import com.example.simplebroker.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Сontroller for creating topics/groups in broker
 */

@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    /**
     * Create topic/group in broker
     * @param deviceName - device/subscriber name
     * @param topicRqDto - topic info
     */

    @PostMapping
    public void create(@RequestBody TopicRqDto topicRqDto,
                       @RequestHeader("X-DEVICE") String deviceName) {
        topicService.create(topicRqDto);
    }

    /**
     * Get all topics/groups in broker
     */

    @GetMapping
    public TopicsRsDto getAll() {
        return topicService.getAllTopics();
    }

    /**
     * Subscribe to topic/group
     * @param deviceName - device/subscriber name
     * @param topicSubscribeRqDto - topic info
     */

    @PostMapping("/subscribe")
    public void subscribe(@RequestBody TopicSubscribeRqDto topicSubscribeRqDto,
                          @RequestHeader("X-DEVICE") String deviceName) {
        topicService.subscribe(topicSubscribeRqDto, deviceName);
    }
}
