package com.onlymeal.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
    private String gender;
    private String birthDate;
    private Double height;
    private Double weight;
    private String activityLevel;
    private String targetGoal;
}