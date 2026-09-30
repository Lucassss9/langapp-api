package com.dev.langapp.dto;

import com.dev.langapp.enums.CefrLevel;
import com.dev.langapp.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(@NotBlank String name,
                             CefrLevel cefrLevel,
                             @NotNull Language targetLanguage,
                             @NotNull Language explanationLanguage) {
}
