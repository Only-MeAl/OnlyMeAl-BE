package com.onlymeal.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoachUpdateRequest {
    @NotNull(message = "코치 어조는 필수입니다")
    private String coachTone;

    @NotNull(message = "코치 성격은 필수입니다")
    private String coachPersonality;
}