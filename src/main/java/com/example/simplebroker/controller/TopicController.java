package com.example.simplebroker.controller;

import com.example.simplebroker.dto.rq.TopicRqDto;
import com.example.simplebroker.dto.rq.TopicSubscribeRqDto;
import com.example.simplebroker.dto.rs.TopicRsDto;
import com.example.simplebroker.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public void create(@RequestBody TopicRqDto topicRqDto,
                       @RequestHeader("X-DEVICE") String deviceName) {
        topicService.create(topicRqDto);
    }

    @GetMapping
    public List<TopicRsDto> getAll() {
        return topicService.getAllTopics();
    }

    @PostMapping("/subscribe")
    public void subscribe(@RequestBody TopicSubscribeRqDto topicSubscribeRqDto,
                          @RequestHeader("X-DEVICE") String deviceName) {
        topicService.subscribe(topicSubscribeRqDto, deviceName);
    }
}
