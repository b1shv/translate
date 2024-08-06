package ru.tinkoff.edu.translate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TranslationRequest {
    private String ip;
    private LocalDateTime dateTime;
    private String from;
    private String to;
    private String words;
    private String wordsTranslated;
}
