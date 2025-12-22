package com.onlymeal.domain.meal.service;

import com.onlymeal.domain.meal.dto.DailySummary;
import com.onlymeal.domain.meal.entity.DailyAnalysis;
import com.onlymeal.domain.meal.entity.MealItem;
import com.onlymeal.domain.rdi.dto.RdiResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class NutrientAccumulator {
    private double totalCalories = 0;
    private double totalCarbs = 0;
    private double totalProtein = 0;
    private double totalFat = 0;
    private double totalSugars = 0;
    private double totalSodium = 0;

    public void add(MealItem item) {
        if (item.getFood() == null) return;

        double multiplier = item.calculateMultiplier();

        this.totalCalories += item.getFood().getCaloriesKcal() * multiplier;
        this.totalCarbs += item.getFood().getCarbsG() * multiplier;
        this.totalProtein += item.getFood().getProteinG() * multiplier;
        this.totalFat += item.getFood().getFatG() * multiplier;
        this.totalSugars += item.getFood().getSugarsG() * multiplier;
        this.totalSodium += item.getFood().getSodiumMg() * multiplier;
    }

    public DailyAnalysis toDailyAnalysis(Long userId, LocalDate date, RdiResponse rdi) {
        double scoreCalories = gaussianScore(totalCalories, rdi.getRecCalories());
        double scoreCarbs = gaussianScore(totalCarbs, rdi.getRecCarbs());
        double scoreProtein = gaussianScore(totalProtein, rdi.getRecProtein());
        double scoreFat = gaussianScore(totalFat, rdi.getRecFat());
        double scoreSugars = gaussianScore(totalSugars, rdi.getRecSugars());
        double scoreSodium = gaussianScoreUpperLimit(totalSodium, rdi.getRecSodium());
        double totalScore = (scoreCalories + scoreCarbs + scoreProtein + scoreFat + scoreSugars + scoreSodium) / 6.0;

        DailyAnalysis analysis = new DailyAnalysis();
        analysis.setUserId(userId);
        analysis.setAnalysisDate(date);
        analysis.setTotalCalories(round(totalCalories));
        analysis.setTotalCarbs(round(totalCarbs));
        analysis.setTotalProtein(round(totalProtein));
        analysis.setTotalFat(round(totalFat));
        analysis.setTotalSugars(round(totalSugars));
        analysis.setTotalSodium(round(totalSodium));
        analysis.setTotalScore(round(totalScore));
        analysis.setScoreCalories(round(scoreCalories));
        analysis.setScoreCarbs(round(scoreCarbs));
        analysis.setScoreProtein(round(scoreProtein));
        analysis.setScoreFat(round(scoreFat));
        analysis.setScoreSugars(round(scoreSugars));
        analysis.setScoreSodium(round(scoreSodium));
        return analysis;
    }

    public DailySummary toDailySummary(RdiResponse rdi) {
        return DailySummary.builder()
                .totalCalories(round(totalCalories))
                .totalCarbs(round(totalCarbs))
                .totalProtein(round(totalProtein))
                .totalFat(round(totalFat))
                .totalSugars(round(totalSugars))
                .totalSodium(round(totalSodium))
                .recCalories(rdi.getRecCalories())
                .recCarbs(rdi.getRecCarbs())
                .recProtein(rdi.getRecProtein())
                .recFat(rdi.getRecFat())
                .recSugars(rdi.getRecSugars())
                .recSodium(rdi.getRecSodium())
                .percentCalories(calculatePercent(totalCalories, rdi.getRecCalories()))
                .percentCarbs(calculatePercent(totalCarbs, rdi.getRecCarbs()))
                .percentProtein(calculatePercent(totalProtein, rdi.getRecProtein()))
                .percentFat(calculatePercent(totalFat, rdi.getRecFat()))
                .percentSugars(calculatePercent(totalSugars, rdi.getRecSugars()))
                .percentSodium(calculatePercent(totalSodium, rdi.getRecSodium()))
                .build();
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private int calculatePercent(double current, double target) {
        if (target == 0) return 0;
        return (int) (current / target * 100);
    }

    private double gaussianScore(double actual, double target) {
        if (target == 0) return 0;
        double ratio = actual / target;
        double sigma = 0.3;
        return 100 * Math.exp(-Math.pow(ratio - 1.0, 2) / (2 * sigma * sigma));
    }

    private double gaussianScoreUpperLimit(double actual, double target) {
        if (target == 0) return 0;
        double ratio = actual / target;
        if (ratio <= 1.0) return 100;
        return gaussianScore(actual, target);
    }
}