package com.onlymeal.domain.meal.entity;

import com.onlymeal.domain.food.entity.Food;
import com.onlymeal.domain.meal.dto.MealItemRequest;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealItem {
    private Long itemId;
    private Long logId;
    private Long foodId;
    private String foodName;
    private String inputType;
    private Double inputAmount;

    private Food food;

    public static List<MealItem> createList(Long logId, List<MealItemRequest> requests) {
        return requests.stream()
                .map(req -> MealItem.builder()
                        .logId(logId)
                        .foodId(req.getFoodId())
                        .inputType(req.getInputType())
                        .inputAmount(req.getInputAmount())
                        .build())
                .collect(Collectors.toList());
    }

    public double calculateMultiplier() {
        if (this.food == null) return 0.0;

        if ("GRAM".equals(this.inputType)) {
            return this.inputAmount / 100.0;
        } else {
            return (this.inputAmount * this.food.getServingUnitG()) / 100.0;
        }
    }
}