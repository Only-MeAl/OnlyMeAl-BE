package com.onlymeal.domain.meal.dto;

import com.onlymeal.domain.meal.entity.DailyAnalysis;
import com.onlymeal.domain.rdi.dto.RdiResponse;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyAnalysisResponse {
    private LocalDate analysisDate;

    // 일일 섭취량
    private Double totalCalories;
    private Double totalCarbs;
    private Double totalProtein;
    private Double totalFat;
    private Double totalSugars;
    private Double totalSodium;

    // RDI 기준값
    private Double recCalories;
    private Double recCarbs;
    private Double recProtein;
    private Double recFat;
    private Double recSugars;
    private Double recSodium;

    // 가우시안 점수
    private Double totalScore;
    private Double scoreCalories;
    private Double scoreCarbs;
    private Double scoreProtein;
    private Double scoreFat;
    private Double scoreSugars;
    private Double scoreSodium;

    public static DailyAnalysisResponse of(DailyAnalysis analysis, RdiResponse rdi) {
        return DailyAnalysisResponse.builder()
                .analysisDate(analysis.getAnalysisDate())
                // 섭취량
                .totalCalories(analysis.getTotalCalories())
                .totalCarbs(analysis.getTotalCarbs())
                .totalProtein(analysis.getTotalProtein())
                .totalFat(analysis.getTotalFat())
                .totalSugars(analysis.getTotalSugars())
                .totalSodium(analysis.getTotalSodium())
                // RDI 기준
                .recCalories(rdi.getRecCalories())
                .recCarbs(rdi.getRecCarbs())
                .recProtein(rdi.getRecProtein())
                .recFat(rdi.getRecFat())
                .recSugars(rdi.getRecSugars())
                .recSodium(rdi.getRecSodium())
                // 점수
                .totalScore(analysis.getTotalScore())
                .scoreCalories(analysis.getScoreCalories())
                .scoreCarbs(analysis.getScoreCarbs())
                .scoreProtein(analysis.getScoreProtein())
                .scoreFat(analysis.getScoreFat())
                .scoreSugars(analysis.getScoreSugars())
                .scoreSodium(analysis.getScoreSodium())
                .build();
    }
}