package com.example.simplebroker.dto.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class DeviceRqDto {
    @NonNull
    private String name;
}
