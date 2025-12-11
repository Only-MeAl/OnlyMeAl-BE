package com.onlymeal.domain.meal.controller;

import com.onlymeal.domain.meal.dto.MealCreateRequest;
import com.onlymeal.domain.meal.dto.MealDetailResponse;
import com.onlymeal.domain.meal.dto.MealUpdateRequest;
import com.onlymeal.domain.meal.service.MealService;
import com.onlymeal.global.common.ApiResponse;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
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

    @PatchMapping("/{logId}")
    public ApiResponse<Void> updateMeal(
            @PathVariable Long logId,
            @AuthenticationPrincipal Long userId,
            @RequestPart(value = "data", required = false) @Valid MealUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        if (request == null && image == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        mealService.updateMeal(logId, userId, request, image);

        return ApiResponse.success(null);
    }

    @DeleteMapping("/{logId}")
    public ApiResponse<Void> deleteMeal(
            @PathVariable Long logId,
            @AuthenticationPrincipal Long userId) {

        mealService.deleteMeal(logId, userId);
        return ApiResponse.success(null);
    }
}