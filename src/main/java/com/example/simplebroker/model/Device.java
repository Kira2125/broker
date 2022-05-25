package com.example.simplebroker.model;

import lombok.Getter;

import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@Getter
public class Device {
    private final String name;
    private Queue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    private Set<String> topics = new ConcurrentSkipListSet<>();

    public Device(String name) {
        this.name = name;
    }

    public void changeMessagesStatusToPending() {
        this.messageQueue.forEach(Message::changeMessageStatusToPending);
    }

    public void deletePendingMessages() {
        this.messageQueue.removeIf(next -> Objects.equals(next.getStatus(), Message.Status.PENDING));
    }
}
