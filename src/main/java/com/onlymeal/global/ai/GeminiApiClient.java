package com.onlymeal.global.ai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiApiClient {

    @Value("${spring.ai.google.genai.api-key}")
    private String apiKey;

    @Value("${spring.ai.google.genai.chat.options.model}")
    private String modelName;

    @Value("${spring.ai.google.genai.base-url}")
    private String baseUrl;

    public String generateContent(String textPrompt, byte[] imageBytes) {
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        GeminiRequest request = new GeminiRequest(
                List.of(new Content("user",
                        List.of(
                                new Part(textPrompt),
                                new Part(new InlineData("image/jpeg", base64Image))
                        )
                ))
        );
        return callApi(request);
    }

    public GeminiResponse generateChatWithToolsAndSystem(
            List<Content> contents,
            List<Tool> tools,
            String systemPrompt) {

        Content systemInstruction = new Content("user", List.of(new Part(systemPrompt)));
        GeminiRequest request = new GeminiRequest(contents, tools, systemInstruction);
        return callApiFullResponse(request);
    }

    private String callApi(GeminiRequest request) {
        GeminiResponse response = callApiFullResponse(request);
        if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            List<Part> parts = response.getCandidates().get(0).getContent().getParts();
            if (parts != null && !parts.isEmpty()) {
                return parts.get(0).getText();
            }
        }
        throw new CustomException(ErrorCode.AI_SERVICE_ERROR);
    }

    private GeminiResponse callApiFullResponse(GeminiRequest request) {
        try {
            RestClient restClient = RestClient.builder().baseUrl(baseUrl).build();

            return restClient.post()
                    .uri("/v1beta/models/" + modelName + ":generateContent?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_SERVICE_ERROR);
        }
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GeminiRequest {
        private final List<Content> contents;
        private final List<Tool> tools;
        private final Content systemInstruction;

        public GeminiRequest(List<Content> contents) {
            this(contents, null, null);
        }

        public GeminiRequest(List<Content> contents, List<Tool> tools) {
            this(contents, tools, null);
        }

        public GeminiRequest(List<Content> contents, List<Tool> tools, Content systemInstruction) {
            this.contents = contents;
            this.tools = tools;
            this.systemInstruction = systemInstruction;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Tool {
        private List<FunctionDeclaration> functionDeclarations;
    }

    @Getter
    @AllArgsConstructor
    public static class FunctionDeclaration {
        private String name;
        private String description;
        private Map<String, Object> parameters; // JSON Schema
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Content {
        private String role;
        private List<Part> parts;

        public Content() {}

        public Content(String role, List<Part> parts) {
            this.role = role;
            this.parts = parts;
        }
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class Part {
        private String text;
        private InlineData inlineData;
        private FunctionCall functionCall;
        private FunctionResponse functionResponse;

        public Part(String text) { this.text = text; }
        public Part(InlineData inlineData) { this.inlineData = inlineData; }
        public Part(FunctionCall functionCall) { this.functionCall = functionCall; }
        public Part(FunctionResponse functionResponse) { this.functionResponse = functionResponse; }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InlineData {
        private String mimeType;
        private String data;
    }

    @Getter
    @NoArgsConstructor
    public static class FunctionCall {
        private String name;
        private Map<String, Object> args;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FunctionResponse {
        private String name;
        private Map<String, Object> response;
    }

    @Getter
    @NoArgsConstructor
    public static class GeminiResponse {
        private List<Candidate> candidates;
    }

    @Getter
    @NoArgsConstructor
    public static class Candidate {
        private Content content;
    }
}