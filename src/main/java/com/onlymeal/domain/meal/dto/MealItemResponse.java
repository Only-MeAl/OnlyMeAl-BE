package com.onlymeal.domain.meal.dto;

import com.onlymeal.domain.meal.entity.MealItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealItemResponse {
    private Long itemId;
    private Long foodId;
    private String inputType;
    private Double inputAmount;

    public static MealItemResponse from(MealItem item) {
        return MealItemResponse.builder()
                .itemId(item.getItemId())
                .foodId(item.getFoodId())
                .inputType(item.getInputType())
                .inputAmount(item.getInputAmount())
                .build();
    }
}