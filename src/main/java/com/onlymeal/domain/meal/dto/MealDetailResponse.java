package com.onlymeal.domain.meal.dto;

import com.onlymeal.domain.meal.entity.MealItem;
import com.onlymeal.domain.meal.entity.MealLog;
import com.onlymeal.domain.meal.service.NutrientAccumulator;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MealDetailResponse {
    private Long logId;
    private String mealType;
    private String mealDate;
    private String imageUrl;
    private List<MealItemResponse> items;

    private Double totalCalories;
    private Double totalCarbs;
    private Double totalProtein;
    private Double totalFat;
    private Double totalSugars;
    private Double totalSodium;

    public static MealDetailResponse of(MealLog mealLog, List<MealItem> mealItems) {
        List<MealItemResponse> items = mealItems.stream()
                .map(MealItemResponse::from)
                .toList();

        NutrientAccumulator accumulator = new NutrientAccumulator();
        for (MealItem item : mealItems) {
            accumulator.add(item);
        }

        return MealDetailResponse.builder()
                .logId(mealLog.getLogId())
                .mealType(mealLog.getMealType())
                .mealDate(mealLog.getMealDate())
                .imageUrl(mealLog.getImageUrl())
                .items(items)
                .totalCalories(round(accumulator.getTotalCalories()))
                .totalCarbs(round(accumulator.getTotalCarbs()))
                .totalProtein(round(accumulator.getTotalProtein()))
                .totalFat(round(accumulator.getTotalFat()))
                .totalSugars(round(accumulator.getTotalSugars()))
                .totalSodium(round(accumulator.getTotalSodium()))
                .build();
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}