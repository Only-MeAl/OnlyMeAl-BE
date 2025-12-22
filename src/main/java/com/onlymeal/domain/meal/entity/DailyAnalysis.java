package com.onlymeal.domain.meal.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyAnalysis {
    private Long analysisId;
    private Long userId;
    private LocalDate analysisDate;

    private Double totalCalories;
    private Double totalCarbs;
    private Double totalProtein;
    private Double totalFat;
    private Double totalSugars;
    private Double totalSodium;

    private Double totalScore;
    private Double scoreCalories;
    private Double scoreCarbs;
    private Double scoreProtein;
    private Double scoreFat;
    private Double scoreSugars;
    private Double scoreSodium;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}