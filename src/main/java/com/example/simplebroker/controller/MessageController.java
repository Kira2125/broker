package com.example.simplebroker.controller;

import com.example.simplebroker.dto.rq.SendMessageBroadcastRqDto;
import com.example.simplebroker.dto.rq.SendMessageDeviceRqDto;
import com.example.simplebroker.dto.rq.SendMessageTopicRqDto;
import com.example.simplebroker.dto.rs.MessagesRsDto;
import com.example.simplebroker.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public void sendMessageToDevice(@RequestBody SendMessageDeviceRqDto sendMessageDeviceRqDto,
                                    @RequestHeader("X-DEVICE") String deviceName) {
        messageService.sendMessagePrivate(sendMessageDeviceRqDto, deviceName);
    }

    @PostMapping("/topic")
    public void sendMessageToTopic(@RequestBody SendMessageTopicRqDto sendMessageTopicRqDto,
                                   @RequestHeader("X-DEVICE") String deviceName) {
        messageService.sendMessageTopic(sendMessageTopicRqDto, deviceName);
    }

    @PostMapping("/broadcast")
    public void sendMessageBroadcast(@RequestBody SendMessageBroadcastRqDto sendMessageBroadcastRqDto,
                                     @RequestHeader("X-DEVICE") String deviceName) {
        messageService.sendMessageBroadcast(sendMessageBroadcastRqDto, deviceName);
    }

    @GetMapping
    public MessagesRsDto getMessages(@RequestHeader("X-DEVICE") String deviceName) {
        return messageService.getMessages(deviceName);
    }

    @DeleteMapping
    public void acknowledgeMessages(@RequestHeader("X-DEVICE") String deviceName) {
        messageService.acknowledgeMessages(deviceName);
    }
}
