package com.onlymeal.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다")
    private String nickname;

    @NotBlank(message = "성별은 필수입니다")
    private String gender;

    @NotBlank(message = "생년월일은 필수입니다")
    private String birthDate;

    @NotNull(message = "키는 필수입니다")
    @Positive(message = "키는 양수여야 합니다")
    private Double height;

    @NotNull(message = "몸무게는 필수입니다")
    @Positive(message = "몸무게는 양수여야 합니다")
    private Double weight;

    @NotBlank(message = "활동량은 필수입니다")
    private String activityLevel;

    @NotBlank(message = "목표는 필수입니다")
    private String targetGoal;
}