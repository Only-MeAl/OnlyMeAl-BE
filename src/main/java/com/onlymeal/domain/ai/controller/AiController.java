package com.onlymeal.domain.ai.controller;

import com.onlymeal.domain.ai.dto.FoodRecognitionResponse;
import com.onlymeal.domain.ai.service.AiService;
import com.onlymeal.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @PostMapping("/recognize-food")
    public ApiResponse<FoodRecognitionResponse> recognizeFood(@RequestParam("image") MultipartFile file) {
        FoodRecognitionResponse response = aiService.recognizeFoodFromImage(file);
        return ApiResponse.success(response);
    }
}