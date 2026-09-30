package com.dev.langapp.dto;

import com.dev.langapp.enums.CefrLevel;
import com.dev.langapp.enums.Language;

import java.util.UUID;

public record StudentResponse(UUID minecraftUuid,
                              String name,
                              CefrLevel cefrLevel,
                              Language targetLanguage,
                              Language explanationLanguage) {
}
