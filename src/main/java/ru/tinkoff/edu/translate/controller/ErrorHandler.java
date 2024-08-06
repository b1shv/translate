package ru.tinkoff.edu.translate.controller;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.translate.exception.UnsupportedLanguageException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({MissingServletRequestParameterException.class, UnsupportedLanguageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupportedLanguage(final Exception e) {
        log.warn(e.getMessage(), e);
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(final Exception e) {
        log.warn(e.getMessage(), e);
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message("An unexpected problem has arisen, please repeat your request later")
                .build();
    }

    @Data
    @Builder
    private static class ErrorResponse {
        private final String status;
        private final String message;
    }
}
