package com.example.simplebroker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Log {
    private final String senderName;
    private final UUID topicId;
    private final UUID messageId;
    private final List<String> keywords;
}
