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

    /**
     * Send message to device/subscriber - one-to-one
     * @param deviceName - device/subscriber name
     * @param sendMessageDeviceRqDto - message body
     */

    @PostMapping
    public void sendMessageToDevice(@RequestBody SendMessageDeviceRqDto sendMessageDeviceRqDto,
                                    @RequestHeader("X-DEVICE") String deviceName) {
        messageService.sendMessageDevice(sendMessageDeviceRqDto, deviceName);
    }

    /**
     * Send message to topic/group
     * @param deviceName - device/subscriber name
     * @param sendMessageTopicRqDto - message body
     */

    @PostMapping("/topic")
    public void sendMessageToTopic(@RequestBody SendMessageTopicRqDto sendMessageTopicRqDto,
                                   @RequestHeader("X-DEVICE") String deviceName) {
        messageService.sendMessageTopic(sendMessageTopicRqDto, deviceName);
    }

    /**
     * Send message to all broker devices/subscribers
     * @param deviceName - device/subscriber name
     * @param sendMessageBroadcastRqDto - message body
     */

    @PostMapping("/broadcast")
    public void sendMessageBroadcast(@RequestBody SendMessageBroadcastRqDto sendMessageBroadcastRqDto,
                                     @RequestHeader("X-DEVICE") String deviceName) {
        messageService.sendMessageBroadcast(sendMessageBroadcastRqDto, deviceName);
    }

    /**
     * Get all new messages for device/subscriber
     * @param deviceName - device/subscriber name
     */

    @GetMapping
    public MessagesRsDto getMessages(@RequestHeader("X-DEVICE") String deviceName) {
        return messageService.getMessages(deviceName);
    }

    /**
     * Acknowledge message receiving to clear queue
     * @param deviceName - device/subscriber name
     */

    @DeleteMapping
    public void acknowledgeMessages(@RequestHeader("X-DEVICE") String deviceName) {
        messageService.acknowledgeMessages(deviceName);
    }
}
