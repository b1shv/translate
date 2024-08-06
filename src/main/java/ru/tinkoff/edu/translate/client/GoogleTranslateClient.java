package ru.tinkoff.edu.translate.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.edu.translate.dto.googleTranslate.GoogleTranslateResponse;
import ru.tinkoff.edu.translate.dto.googleTranslate.Language;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GoogleTranslateClient implements TranslateClient {
    private final RestTemplate restTemplate;
    @Value("${google-translate.url}")
    private String baseUrl;

    @Value("${google-translate.key}")
    private String apiKey;

    public GoogleTranslateClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public Set<String> getLanguages() {
        ResponseEntity<GoogleTranslateResponse> responseEntity = restTemplate.getForEntity(
                String.format("%s/languages?key=%s", baseUrl, apiKey), GoogleTranslateResponse.class);

        return responseEntity.getBody().getData().getLanguages().stream()
                .map(Language::getLanguage)
                .collect(Collectors.toSet());
    }

    @Override
    public String translateWord(String word, String from, String to) {
        Map<String, String> params = new HashMap<>();
        params.put("source", from);
        params.put("target", to);
        params.put("q", word);
        params.put("key", apiKey);

        ResponseEntity<GoogleTranslateResponse> responseEntity = restTemplate.postForEntity(
                String.format("%s?key={key}&source={source}&target={target}&q={q}", baseUrl), null, GoogleTranslateResponse.class, params);

        return responseEntity.getBody().getData().getFirstTranslation();
    }
}
