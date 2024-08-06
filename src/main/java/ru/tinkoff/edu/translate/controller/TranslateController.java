package ru.tinkoff.edu.translate.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.translate.dto.TranslationRequest;
import ru.tinkoff.edu.translate.dto.TranslationResponse;
import ru.tinkoff.edu.translate.service.TranslateService;

import java.sql.SQLException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/translate")
@RequiredArgsConstructor
@Slf4j
public class TranslateController {
    private final TranslateService translateService;

    @PostMapping
    public TranslationResponse translateWords(@RequestParam String from,
                                              @RequestParam String to,
                                              @RequestParam String words,
                                              HttpServletRequest request) throws SQLException {
        log.info(String.format("/translate POST from=%s to=%s words=%s", from, to, words));
        return toResponse(translateService.translate(
                TranslationRequest.builder()
                        .ip(request.getRemoteAddr())
                        .dateTime(LocalDateTime.now())
                        .from(from)
                        .to(to)
                        .words(words)
                        .build()
        ));
    }

    private TranslationResponse toResponse(TranslationRequest request) {
        return new TranslationResponse(request.getWordsTranslated());
    }
}
