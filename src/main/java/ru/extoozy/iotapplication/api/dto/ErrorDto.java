package ru.extoozy.iotapplication.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {

    private String message;

    @Builder.Default
    private Map<String, String> errors = new HashMap<>();

}
