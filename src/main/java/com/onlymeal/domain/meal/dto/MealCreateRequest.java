package com.onlymeal.domain.meal.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MealCreateRequest {
    @NotBlank(message = "식사 구분은 필수입니다")
    private String mealType;

    @NotBlank(message = "식사 날짜는 필수입니다")
    private String mealDate;

    @NotEmpty(message = "음식 항목은 최소 1개 이상이어야 합니다")
    @Valid
    private List<MealItemRequest> items;
}