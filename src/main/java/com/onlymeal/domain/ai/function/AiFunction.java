package com.onlymeal.domain.ai.function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AiFunction {
    GET_DAILY_MEAL_LOG("get_daily_meal_log", "식단 기록 조회"),
    GET_ANALYSIS_SCORE("get_analysis_score", "분석 점수 조회"),
    SEARCH_FOOD_IN_DB("search_food_in_db", "음식 검색");

    private final String functionName;
    private final String description;

    public static AiFunction from(String name) {
        return Arrays.stream(values())
                .filter(func -> func.functionName.equals(name))
                .findFirst()
                .orElse(null);
    }
}