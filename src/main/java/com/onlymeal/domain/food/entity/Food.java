package com.onlymeal.domain.food.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Food {
    private Long foodId;
    private String foodName;
    private Double servingUnitG;
    private Double caloriesKcal;
    private Double carbsG;
    private Double proteinG;
    private Double fatG;
    private Double sugarsG;
    private Double sodiumMg;
}