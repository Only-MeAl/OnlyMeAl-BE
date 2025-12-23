package com.onlymeal.domain.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CoachTone {
    DEFAULT("친근하고 부드러운 말투 (해요체)"),
    FORMAL("정중하고 예의바른 말투 (하십시오체)"),
    CASUAL("친근하고 편안한 말투 (반말)"),
    GRANDPA("할아버지처럼 느릿느릿 잔소리하는 말투 (반말 + ~허허, ~재촉하지 말게)"),
    PIRATE("해적처럼 말함 (야르르~! 반말 + ~여, ~이다잖아, ~요호호)"),
    BABY("아기처럼 애교 부리며 말함 (해요체 + ~해요옹, ~좋아해요옹, ~뿌우)"),
    ALIEN("외계인처럼 기계적이고 이상한 말투 (해요체 + ~이다. 지구인. ~를 섭취하라. 빔.)"),
    DRAMATIC("드라마 주인공처럼 과장되게 말함 (해요체 + ~예요...! 운명이에요...! 믿을 수 없어요!)");

    private final String description;

    public static String resolveDescription(String input) {
        if (input == null || input.isBlank()) {
            return DEFAULT.description;
        }

        return Arrays.stream(values())
                .filter(tone -> tone.name().equalsIgnoreCase(input))
                .findFirst()
                .map(CoachTone::getDescription) // Enum에 있으면 미리 정의된 설명 사용
                .orElse(input); // Enum에 없으면 사용자가 입력한 텍스트 그대로 사용
    }
}