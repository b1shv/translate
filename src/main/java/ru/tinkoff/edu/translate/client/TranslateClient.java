package ru.tinkoff.edu.translate.client;

import java.util.Set;

public interface TranslateClient {
    Set<String> getLanguages();

    String translateWord(String word, String from, String to);
}
