package com.onlymeal.domain.ai.dto;

import com.onlymeal.domain.food.dto.FoodDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoodRecognitionResponse {
    private List<FoodDetailResponse> foods;
}