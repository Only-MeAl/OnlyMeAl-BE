package com.onlymeal.domain.user.dto;

import com.onlymeal.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private String email;
    private String nickname;
    private String gender;
    private String birthDate;
    private Double height;
    private Double weight;
    private String activityLevel;
    private String targetGoal;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .height(user.getHeight())
                .weight(user.getWeight())
                .activityLevel(user.getActivityLevel())
                .targetGoal(user.getTargetGoal())
                .build();
    }
}