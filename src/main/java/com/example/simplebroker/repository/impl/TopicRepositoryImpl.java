package com.example.simplebroker.repository.impl;

import com.example.simplebroker.repository.TopicRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class TopicRepositoryImpl implements TopicRepository {
    private final List<String> topics = new CopyOnWriteArrayList<>();

    public void add(String topic) {
        topics.add(topic);
    }

    public List<String> getAll() {
        return new CopyOnWriteArrayList<>(topics);
    }

    public boolean exist(String topic) {
        return topics.contains(topic);
    }
}
