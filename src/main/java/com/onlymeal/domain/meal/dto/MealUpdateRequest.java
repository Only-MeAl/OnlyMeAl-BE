package com.onlymeal.domain.meal.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MealUpdateRequest {
    private String mealType;

    @Valid
    private List<MealItemRequest> items;
}