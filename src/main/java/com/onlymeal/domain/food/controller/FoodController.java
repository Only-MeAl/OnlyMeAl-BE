package com.onlymeal.domain.food.controller;

import com.onlymeal.domain.food.dto.FoodDetailResponse;
import com.onlymeal.domain.food.service.FoodService;
import com.onlymeal.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/search")
    public ApiResponse<List<FoodDetailResponse>> searchFood(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10") int limit) {

        List<FoodDetailResponse> response = foodService.searchFood(keyword, limit);
        return ApiResponse.success(response);
    }
}