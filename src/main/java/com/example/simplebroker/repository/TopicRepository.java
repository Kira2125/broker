package com.example.simplebroker.repository;

import com.example.simplebroker.enums.TopicType;
import com.example.simplebroker.model.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {

    Optional<Topic> getByName(String name);

    Optional<Topic> getByNameAndType(String name, TopicType type);

    List<Topic> getByType(TopicType type);
}
