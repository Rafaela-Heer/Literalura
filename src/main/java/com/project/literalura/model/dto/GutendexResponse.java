package com.project.literalura.model.dto;

import java.util.List;

public record GutendexResponse(
        int count,
        String next,
        String previous,
        List<GutenBookDto> results
) {}