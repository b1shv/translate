package ru.tinkoff.edu.translate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TranslationResponse {
    private String wordsTranslated;
}
