package ru.tinkoff.edu.translate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.translate.client.TranslateClient;
import ru.tinkoff.edu.translate.dto.TranslationRequest;
import ru.tinkoff.edu.translate.exception.UnsupportedLanguageException;
import ru.tinkoff.edu.translate.repository.TranslateRepository;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslateServiceImplTest {
    @Mock
    private TranslateClient translateClient;

    @Mock
    private TranslateRepository translateRepository;

    @InjectMocks
    private TranslateServiceImpl translateService;

    private final TranslationRequest request = TranslationRequest.builder()
            .ip("127.0.0.1")
            .from("ru")
            .to("en")
            .words("кот пёс конь")
            .build();

    @Test
    void translate_shouldReturnTranslation() throws SQLException {
        TranslationRequest requestExpected = TranslationRequest.builder()
                .ip(request.getIp())
                .from(request.getFrom())
                .to(request.getTo())
                .words(request.getWords())
                .wordsTranslated("cat dog horse")
                .build();

        Set<String> languages = new HashSet<>();
        languages.add(request.getFrom());
        languages.add(request.getTo());

        when(translateClient.getLanguages()).thenReturn(languages);
        when(translateClient.translateWord("кот", request.getFrom(), request.getTo())).thenReturn("cat");
        when(translateClient.translateWord("пёс", request.getFrom(), request.getTo())).thenReturn("dog");
        when(translateClient.translateWord("конь", request.getFrom(), request.getTo())).thenReturn("horse");

        assertThat(translateService.translate(request)).isEqualTo(requestExpected);
        verify(translateClient, times(1)).getLanguages();
        verify(translateClient, times(1)).translateWord("кот", request.getFrom(), request.getTo());
        verify(translateClient, times(1)).translateWord("пёс", request.getFrom(), request.getTo());
        verify(translateClient, times(1)).translateWord("конь", request.getFrom(), request.getTo());
    }

    @Test
    void translate_shouldThrowException_ifLanguageNotSupported() throws SQLException {
        Set<String> languages = new HashSet<>();
        languages.add("en");
        languages.add("fr");
        languages.add("it");

        when(translateClient.getLanguages()).thenReturn(languages);
        assertThatThrownBy(() -> translateService.translate(request)).isInstanceOf(UnsupportedLanguageException.class);
        verify(translateClient, never()).translateWord(anyString(), anyString(), anyString());
        verify(translateRepository, never()).addRequest(request);
    }
}
