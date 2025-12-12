package com.onlymeal.domain.meal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMealStatus {
    private String date;
    private boolean hasBreakfast;
    private boolean hasLunch;
    private boolean hasDinner;
    private boolean hasSnack;
}