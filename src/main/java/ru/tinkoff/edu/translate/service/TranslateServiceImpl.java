package ru.tinkoff.edu.translate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.translate.client.TranslateClient;
import ru.tinkoff.edu.translate.dto.TranslationRequest;
import ru.tinkoff.edu.translate.exception.UnsupportedLanguageException;
import ru.tinkoff.edu.translate.repository.TranslateRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class TranslateServiceImpl implements TranslateService {
    private static final int THREADS_LIMIT = 10;
    private static final String UNSUPPORTED_LANGUAGE_MESSAGE = "The '%s' language is not supported";
    private final TranslateClient translateClient;
    private final TranslateRepository translateRepository;

    @Override
    public TranslationRequest translate(TranslationRequest request) throws SQLException {
        checkLanguages(request);
        request.setWordsTranslated(translateWords(request.getWords(), request.getFrom(), request.getTo()));
        translateRepository.addRequest(request);

        return request;
    }

    private void checkLanguages(TranslationRequest request) {
        Set<String> availableLanguages = translateClient.getLanguages();

        if (!availableLanguages.contains(request.getFrom())) {
            throw new UnsupportedLanguageException(String.format(UNSUPPORTED_LANGUAGE_MESSAGE, request.getFrom()));
        }
        if (!availableLanguages.contains(request.getTo())) {
            throw new UnsupportedLanguageException(String.format(UNSUPPORTED_LANGUAGE_MESSAGE, request.getTo()));
        }
    }

    private String translateWords(String words, String from, String to) {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_LIMIT);
        List<CompletableFuture<String>> cfs = new ArrayList<>();

        for (String word : words.split(" ")) {
            cfs.add(CompletableFuture.supplyAsync(() -> translateClient.translateWord(word, from, to), executor));
        }

        StringBuilder builder = new StringBuilder();
        CompletableFuture<?>[] cfsArray = cfs.toArray(new CompletableFuture<?>[0]);

        CompletableFuture.allOf(cfsArray)
                .thenAccept(v -> cfs.forEach(cf -> {
                    builder.append(cf.join());
                    builder.append(" ");
                }))
                .join();

        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
