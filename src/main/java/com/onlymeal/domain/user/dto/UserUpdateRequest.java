package com.onlymeal.domain.user.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
    private String nickname;
    private String password;
    private String gender;
    private String birthDate;

    @Positive(message = "키는 양수여야 합니다")
    private Double height;

    @Positive(message = "몸무게는 양수여야 합니다")
    private Double weight;

    private String activityLevel;
    private String targetGoal;
}