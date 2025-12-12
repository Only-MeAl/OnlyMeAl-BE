package com.onlymeal.domain.meal.service;

import com.onlymeal.domain.meal.dto.DailySummary;
import com.onlymeal.domain.meal.entity.MealItem;
import com.onlymeal.domain.rdi.dto.RdiResponse;
import lombok.NoArgsConstructor;

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
}