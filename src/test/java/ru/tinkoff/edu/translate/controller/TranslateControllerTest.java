package ru.tinkoff.edu.translate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.edu.translate.dto.TranslationRequest;
import ru.tinkoff.edu.translate.dto.TranslationResponse;
import ru.tinkoff.edu.translate.service.TranslateService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TranslateController.class)
class TranslateControllerTest {
    @MockBean
    TranslateService translateService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void translateWords_shouldReturnTranslation() throws Exception {
        TranslationResponse response = new TranslationResponse("аптека улица фонарь");
        TranslationRequest request = TranslationRequest.builder()
                .wordsTranslated(response.getWordsTranslated())
                .build();

        when(translateService.translate(any(TranslationRequest.class))).thenReturn(request);

        mockMvc.perform(post("/translate")
                        .param("from", "en")
                        .param("to", "ru")
                        .param("words", "some words"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wordsTranslated").value(response.getWordsTranslated()));

        verify(translateService, times(1)).translate(any(TranslationRequest.class));
    }
}
