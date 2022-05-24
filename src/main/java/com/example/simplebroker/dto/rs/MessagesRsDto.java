package com.example.simplebroker.dto.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.LinkedBlockingQueue;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class MessagesRsDto {
    private LinkedBlockingQueue<MessageDto> messages;
}
