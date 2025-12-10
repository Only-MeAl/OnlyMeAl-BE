package com.onlymeal.domain.food.dto;

import com.onlymeal.domain.food.entity.Food;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodDetailResponse {
    private Long foodId;
    private String foodName;
    private Double servingUnitG;
    private Double caloriesKcal;
    private Double carbsG;
    private Double proteinG;
    private Double fatG;
    private Double sugarsG;
    private Double sodiumMg;

    public static FoodDetailResponse from(Food food) {
        return FoodDetailResponse.builder()
                .foodId(food.getFoodId())
                .foodName(food.getFoodName())
                .servingUnitG(food.getServingUnitG())
                .caloriesKcal(food.getCaloriesKcal())
                .carbsG(food.getCarbsG())
                .proteinG(food.getProteinG())
                .fatG(food.getFatG())
                .sugarsG(food.getSugarsG())
                .sodiumMg(food.getSodiumMg())
                .build();
    }
}