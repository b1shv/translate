package ru.tinkoff.edu.translate.service;

import ru.tinkoff.edu.translate.dto.TranslationRequest;

import java.sql.SQLException;

public interface TranslateService {
    TranslationRequest translate(TranslationRequest request) throws SQLException;
}
