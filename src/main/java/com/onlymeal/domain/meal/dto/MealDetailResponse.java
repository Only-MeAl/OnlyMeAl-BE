package com.onlymeal.domain.meal.dto;

import com.onlymeal.domain.meal.entity.MealItem;
import com.onlymeal.domain.meal.entity.MealLog;
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

    public static MealDetailResponse of(MealLog mealLog, List<MealItem> mealItems) {
        List<MealItemResponse> items = mealItems.stream()
                .map(MealItemResponse::from)
                .toList();

        return MealDetailResponse.builder()
                .logId(mealLog.getLogId())
                .mealType(mealLog.getMealType())
                .mealDate(mealLog.getMealDate())
                .imageUrl(mealLog.getImageUrl())
                .items(items)
                .build();
    }
}