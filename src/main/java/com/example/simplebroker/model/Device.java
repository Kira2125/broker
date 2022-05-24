package com.example.simplebroker.model;

import lombok.Getter;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

@Getter
public class Device {
    private final String name;
    private Queue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    private Set<String> topics = new ConcurrentSkipListSet<>();

    public Device(String name) {
        this.name = name;
    }
}
