package com.onlymeal.domain.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String gender;
    private String birthDate;
    private Double height;
    private Double weight;
    private String activityLevel;
    private String targetGoal;
    private String coachTone;
    private String coachPersonality;
    private String createdAt;
}