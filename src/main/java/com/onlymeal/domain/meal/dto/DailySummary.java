package com.onlymeal.domain.meal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySummary {
    private Double totalCalories;
    private Double totalCarbs;
    private Double totalProtein;
    private Double totalFat;
    private Double totalSugars;
    private Double totalSodium;

    private Double recCalories;
    private Double recCarbs;
    private Double recProtein;
    private Double recFat;
    private Double recSugars;
    private Double recSodium;

    private Integer percentCalories;
    private Integer percentCarbs;
    private Integer percentProtein;
    private Integer percentFat;
    private Integer percentSugars;
    private Integer percentSodium;
}