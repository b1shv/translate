package ru.tinkoff.edu.translate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.translate.dto.TranslationRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
@Slf4j
public class TranslateRepositoryImpl implements TranslateRepository {
    private final ConnectionManager connectionManager;

    @Override
    public void addRequest(TranslationRequest request) throws SQLException {
        String sql = "INSERT INTO requests (ip, date_time, words, words_translated) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, request.getIp());
            statement.setTimestamp(2, Timestamp.valueOf(request.getDateTime()));
            statement.setString(3, request.getWords());
            statement.setString(4, request.getWordsTranslated());

            statement.executeUpdate();
        }

    }
}
