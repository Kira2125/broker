package com.example.simplebroker.repository.impl;

import com.example.simplebroker.model.Log;
import com.example.simplebroker.repository.LogRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class LogRepositoryImpl implements LogRepository {

    private final List<Log> logs = new CopyOnWriteArrayList<>();

    @Override
    public void save(Log log) {
        logs.add(log);
    }
}
