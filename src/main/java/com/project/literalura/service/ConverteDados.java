package com.project.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ConverteDados implements IConverteDados {

    private final ObjectMapper mapper;


    public ConverteDados() {
        this.mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        if (json == null || json.isBlank()) {
            throw new RuntimeException("JSON vazio recebido no ConverteDados");
        }

        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Falha ao converter JSON", e);
        }
    }
}