package com.onlymeal.global.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GeminiApiClient {

    @Value("${spring.ai.google.genai.api-key}")
    private String apiKey;

    @Value("${spring.ai.google.genai.chat.options.model}")
    private String modelName;

    @Value("${spring.ai.google.genai.base-url}")
    private String baseUrl;

    /**
     * [멀티모달] 텍스트 + 이미지 요청 (음식 인식용)
     */
    public String generateContent(String textPrompt, byte[] imageBytes) {
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        GeminiRequest request = new GeminiRequest(
                List.of(new Content(
                        List.of(
                                new TextPart(textPrompt),
                                new InlineDataPart(new InlineData("image/jpeg", base64Image))
                        )
                ))
        );
        return callApi(request);
    }

    /**
     * [텍스트] 텍스트 전용 요청 (챗봇용 - 추후 사용)
     */
    public String generateContent(String textPrompt) {
        GeminiRequest request = new GeminiRequest(
                List.of(new Content(
                        List.of(new TextPart(textPrompt))
                ))
        );
        return callApi(request);
    }

    private String callApi(GeminiRequest request) {
        try {
            RestClient restClient = RestClient.builder().baseUrl(baseUrl).build();

            GeminiResponse response = restClient.post()
                    .uri("/v1beta/models/" + modelName + ":generateContent?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                return response.getCandidates().get(0).getContent().getParts().get(0).getText();
            }
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
    }

    @Getter static class GeminiRequest {
        private final List<Content> contents;
        public GeminiRequest(List<Content> contents) { this.contents = contents; }
    }
    @Getter static class Content {
        private final List<Object> parts;
        public Content(List<Object> parts) { this.parts = parts; }
    }
    @Getter static class TextPart {
        private final String text;
        public TextPart(String text) { this.text = text; }
    }
    @Getter static class InlineDataPart {
        @JsonProperty("inline_data")
        private final InlineData inlineData;
        public InlineDataPart(InlineData inlineData) { this.inlineData = inlineData; }
    }
    @Getter static class InlineData {
        @JsonProperty("mime_type")
        private final String mimeType;
        private final String data;
        public InlineData(String mimeType, String data) {
            this.mimeType = mimeType;
            this.data = data;
        }
    }
    @Getter static class GeminiResponse {
        private List<Candidate> candidates;
    }
    @Getter static class Candidate {
        private ContentResponse content;
    }
    @Getter static class ContentResponse {
        private List<TextPart> parts;
    }
}