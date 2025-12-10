package com.onlymeal.domain.rdi.dto;

import com.onlymeal.domain.rdi.entity.RdiStandard;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RdiResponse {
    private Double recCalories;
    private Double recCarbs;
    private Double recProtein;
    private Double recFat;
    private Double recSugars;
    private Double recSodium;

    public static RdiResponse from(RdiStandard rdiStandard, double totalMultiplier) {
        return RdiResponse.builder()
                .recCalories(round(rdiStandard.getRecCalories() * totalMultiplier))
                .recCarbs(round(rdiStandard.getRecCarbs() * totalMultiplier))
                .recProtein(round(rdiStandard.getRecProtein() * totalMultiplier))
                .recFat(round(rdiStandard.getRecFat() * totalMultiplier))
                .recSugars(round(rdiStandard.getRecSugars()))
                .recSodium(round(rdiStandard.getRecSodium()))
                .build();
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}