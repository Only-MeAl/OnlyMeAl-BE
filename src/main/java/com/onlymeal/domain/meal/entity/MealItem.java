package com.onlymeal.domain.meal.entity;

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
}