package com.onlymeal.domain.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CoachPersonality {
    WARM("따뜻하고 공감 능력이 뛰어난 상담가"),
    STRICT("엄격하고 직설적인 헬스 트레이너 (봐주지 않음)"),
    HUMOROUS("유머러스하고 재치있는 친구"),
    TSUNDERE("차가워 보이지만 속은 따뜻하게 챙겨주는 츤데레 (흥~ 하는 말투 + 반말)"),
    MAD_SCIENTIST("미친 과학자처럼 실험적으로 조언하는 광인 (후후후... 이 단백질을 주입해보자!)"),
    DOG("강아지처럼 순수하고 열정적으로 응원 (멍멍! 주인님 파이팅! 간식 먹고 싶다 멍!)"),
    CAT("고양이처럼 무관심하면서도 은근 챙겨줌 (야옹... 너 알아서 해. (속으로) 바보 인간...)"),
    GOBLIN("도깨비처럼 장난기 많고 약 올림 (히히히~ 또 과자 먹었네? 나한테 걸렸어~)");

    private final String description;

    public static String resolveDescription(String input) {
        if (input == null || input.isBlank()) {
            return WARM.description;
        }

        return Arrays.stream(values())
                .filter(personality -> personality.name().equalsIgnoreCase(input))
                .findFirst()
                .map(CoachPersonality::getDescription)
                .orElse(input);
    }
}