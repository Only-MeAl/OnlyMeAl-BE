package com.onlymeal.domain.user.dto;

import com.onlymeal.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoachResponse {
    private String coachTone;
    private String coachPersonality;

    public static CoachResponse from(User user) {
        return CoachResponse.builder()
                .coachTone(user.getCoachTone())
                .coachPersonality(user.getCoachPersonality())
                .build();
    }
}