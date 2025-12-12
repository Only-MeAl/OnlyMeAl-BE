package com.onlymeal.domain.meal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MealDashboardResponse {
    private List<MealDetailResponse> meals;
    private DailySummary dailySummary;

}