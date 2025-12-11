package com.onlymeal.domain.meal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MealItemRequest {
    @NotNull(message = "음식 ID는 필수입니다")
    private Long foodId;

    @NotNull(message = "입력 단위는 필수입니다")
    private String inputType;

    @NotNull(message = "입력량은 필수입니다")
    @Positive(message = "입력량은 양수여야 합니다")
    private Double inputAmount;
}