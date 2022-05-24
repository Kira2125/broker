package com.example.simplebroker.dto.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class TopicsRsDto {
    private List<TopicDto> topics;
}
