package com.example.simplebroker.repository;

import java.util.List;

public interface TopicRepository {

    void add(String topic);

    List<String> getAll();

    boolean exist(String topic);
}
