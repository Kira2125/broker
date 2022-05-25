package com.example.simplebroker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Message {
    private final String message;
    private String topic;
    private final String senderDeviceName;
    private final Timestamp date;
    @Builder.Default
    private Status status=Status.STABLE;

    public enum Status{
        PENDING, STABLE
    }

    public void changeMessageStatusToPending() {
        this.status=Status.PENDING;
    }
}
