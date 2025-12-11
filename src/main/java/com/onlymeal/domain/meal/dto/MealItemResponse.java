package com.onlymeal.domain.meal.dto;

import com.onlymeal.domain.meal.entity.MealItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealItemResponse {
    private Long itemId;
    private Long foodId;
    private String foodName;

    private String inputType;
    private Double inputAmount;

    public static MealItemResponse from(MealItem mealItem) {
        return MealItemResponse.builder()
                .itemId(mealItem.getItemId())
                .foodId(mealItem.getFoodId())
                .foodName(mealItem.getFoodName())
                .inputType(mealItem.getInputType())
                .inputAmount(mealItem.getInputAmount())
                .build();
    }
}