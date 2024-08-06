package ru.tinkoff.edu.translate.repository;

import ru.tinkoff.edu.translate.dto.TranslationRequest;

import java.sql.SQLException;

public interface TranslateRepository {
    void addRequest(TranslationRequest request) throws SQLException;
}
