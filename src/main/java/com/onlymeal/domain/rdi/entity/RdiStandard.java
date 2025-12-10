package com.onlymeal.domain.rdi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RdiStandard {
    private Long rdiId;
    private String gender;
    private Integer ageStart;
    private Integer ageEnd;
    private Double recCalories;
    private Double recCarbs;
    private Double recProtein;
    private Double recFat;
    private Double recSugars;
    private Double recSodium;
}