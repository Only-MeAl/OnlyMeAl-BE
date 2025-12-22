package com.onlymeal.domain.meal.controller;

import com.onlymeal.domain.meal.dto.*;
import com.onlymeal.domain.meal.entity.DailyAnalysis;
import com.onlymeal.domain.meal.service.MealService;
import com.onlymeal.domain.rdi.dto.RdiResponse;
import com.onlymeal.domain.rdi.service.RdiService;
import com.onlymeal.global.common.ApiResponse;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final RdiService rdiService;

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

    @GetMapping
    public ApiResponse<MealDashboardResponse> getDashboard(
            @AuthenticationPrincipal Long userId,
            @RequestParam LocalDate date) {

        MealDashboardResponse response = mealService.getDashboard(userId, date.toString());
        return ApiResponse.success(response);
    }

    @GetMapping("/calendar")
    public ApiResponse<List<DailyMealStatus>> getMonthlyMealStatus(
            @AuthenticationPrincipal Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        List<DailyMealStatus> response = mealService.getDailyMealStatus(
                userId, startDate.toString(), endDate.toString());

        return ApiResponse.success(response);
    }

    @GetMapping("/daily-analysis")
    public ResponseEntity<?> getDailyAnalysis(
            @AuthenticationPrincipal Long userId,
            @RequestParam LocalDate date) {

        DailyAnalysis analysis = mealService.getDailyAnalysis(userId, date.toString());

        if (analysis == null) {
            return ResponseEntity.noContent().build();
        }

        RdiResponse rdi = rdiService.getRdi(userId);
        return ResponseEntity.ok(ApiResponse.success(DailyAnalysisResponse.of(analysis, rdi)));
    }

    @PostMapping("/daily-analysis")
    public ApiResponse<DailyAnalysisResponse> analyzeDailyMeal(
            @AuthenticationPrincipal Long userId,
            @RequestParam LocalDate date) {

        return ApiResponse.success(mealService.analyzeDailyMeal(userId, date.toString()));
    }
}