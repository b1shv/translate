package ru.tinkoff.edu.translate.exception;

public class UnsupportedLanguageException extends RuntimeException {
    public UnsupportedLanguageException() {
    }

    public UnsupportedLanguageException(String message) {
        super(message);
    }
}
