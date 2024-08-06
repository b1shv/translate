package ru.tinkoff.edu.translate.dto.googleTranslate;

import lombok.Data;

import java.util.List;

@Data
public class GoogleTranslateData {
    private List<Translation> translations;
    private List<Language> languages;

    public String getFirstTranslation() {
        return translations.get(0).getTranslatedText();
    }
}
