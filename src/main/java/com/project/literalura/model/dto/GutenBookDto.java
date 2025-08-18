package com.project.literalura.model.dto;

import java.util.List;

public record GutenBookDto(
        int id,
        String title,
        List<GutenAuthorDto> authors,
        List<String> languages,
        Integer download_count
){}