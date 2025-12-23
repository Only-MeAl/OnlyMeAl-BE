package com.onlymeal.domain.ai.service;

import com.onlymeal.domain.ai.dto.FoodRecognitionResponse;
import com.onlymeal.domain.food.dto.FoodDetailResponse;
import com.onlymeal.domain.food.service.FoodService;
import com.onlymeal.global.ai.GeminiApiClient;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import com.onlymeal.global.util.ImageUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodRecognitionService {

    private final GeminiApiClient geminiClient;
    private final FoodService foodService;

    @Value("${ai.prompts.food-system}")
    private Resource foodSystemPromptResource;

    private String foodSystemPrompt;

    @PostConstruct
    public void init() {
        try {
            this.foodSystemPrompt = StreamUtils.copyToString(
                    foodSystemPromptResource.getInputStream(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            log.error("음식 인식 프롬프트 로드 실패", e);
            throw new CustomException(ErrorCode.PROMPT_LOAD_FAILED);
        }
    }

    public FoodRecognitionResponse recognizeFood(MultipartFile file) {
        try {
            byte[] compressedBytes = ImageUtils.compressImage(file);
            String rawAnswer = geminiClient.generateContent(foodSystemPrompt, compressedBytes);
            List<String> foodNames = parseFoodNames(rawAnswer);
            List<FoodDetailResponse> matchedFoods = findFoodsInDatabase(foodNames);
            return new FoodRecognitionResponse(matchedFoods);
        } catch (IOException e) {
            log.error("이미지 처리 실패", e);
            throw new CustomException(ErrorCode.IMAGE_PROCESSING_FAILED);
        }
    }

    private List<String> parseFoodNames(String rawText) {
        if (rawText == null || rawText.isBlank()) return List.of();
        return Arrays.stream(rawText.replace("\n", ",").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private List<FoodDetailResponse> findFoodsInDatabase(List<String> foodNames) {
        List<FoodDetailResponse> matchedFoods = new ArrayList<>();
        for (String name : foodNames) {
            List<FoodDetailResponse> searchResults = foodService.searchFood(name, 1);
            if (!searchResults.isEmpty()) matchedFoods.add(searchResults.get(0));
        }
        return matchedFoods;
    }
}