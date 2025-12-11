package com.onlymeal.domain.meal.controller;

import com.onlymeal.domain.meal.dto.MealCreateRequest;
import com.onlymeal.domain.meal.dto.MealDetailResponse;
import com.onlymeal.domain.meal.service.MealService;
import com.onlymeal.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ApiResponse<Void> createMeal(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestPart("data") MealCreateRequest request,
            @RequestParam("image") MultipartFile image) {

        mealService.createMeal(userId, request, image);
        return ApiResponse.success(null);
    }

    @GetMapping("/{logId}")
    public ApiResponse<MealDetailResponse> getMealDetail(
            @PathVariable Long logId,
            @AuthenticationPrincipal Long userId) {
        return ApiResponse.success(mealService.getMealDetail(logId, userId));
    }
}