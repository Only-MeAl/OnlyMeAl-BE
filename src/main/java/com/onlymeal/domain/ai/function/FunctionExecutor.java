package com.onlymeal.domain.ai.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlymeal.domain.ai.util.DateParser;
import com.onlymeal.domain.food.dto.FoodDetailResponse;
import com.onlymeal.domain.food.service.FoodService;
import com.onlymeal.domain.meal.dto.MealDashboardResponse;
import com.onlymeal.domain.meal.entity.DailyAnalysis;
import com.onlymeal.domain.meal.service.MealService;
import com.onlymeal.global.ai.GeminiApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FunctionExecutor {

    private final FoodService foodService;
    private final MealService mealService;
    private final ObjectMapper objectMapper;

    public List<GeminiApiClient.Tool> defineTools() {
        return List.of(new GeminiApiClient.Tool(List.of(
                new GeminiApiClient.FunctionDeclaration(
                        AiFunction.GET_DAILY_MEAL_LOG.getFunctionName(),
                        "특정 날짜의 식단 기록과 영양 섭취량을 조회합니다. 사용자가 '먹은 거 알려줘', '오늘 식단 어때' 등을 물을 때 사용합니다.",
                        Map.of(
                                "type", "OBJECT",
                                "properties", Map.of(
                                        "date", Map.of("type", "STRING", "description", "조회할 날짜 (YYYY-MM-DD). 언급이 없으면 오늘 날짜 사용")
                                ),
                                "required", List.of("date")
                        )
                ),
                new GeminiApiClient.FunctionDeclaration(
                        AiFunction.GET_ANALYSIS_SCORE.getFunctionName(),
                        "식단 분석 점수를 조회합니다. '점수 알려줘', '잘 먹었는지 평가해줘' 등의 질문에 사용합니다.",
                        Map.of(
                                "type", "OBJECT",
                                "properties", Map.of(
                                        "date", Map.of("type", "STRING", "description", "조회할 날짜 (YYYY-MM-DD)")
                                ),
                                "required", List.of("date")
                        )
                ),
                new GeminiApiClient.FunctionDeclaration(
                        AiFunction.SEARCH_FOOD_IN_DB.getFunctionName(),
                        "음식 데이터베이스에서 음식을 검색합니다. 추천할 메뉴의 정확한 영양 정보를 찾을 때 사용합니다.",
                        Map.of(
                                "type", "OBJECT",
                                "properties", Map.of(
                                        "keyword", Map.of("type", "STRING", "description", "검색할 음식 이름 (예: '닭가슴살', '샐러드')")
                                ),
                                "required", List.of("keyword")
                        )
                )
        )));
    }

    public Map<String, Object> execute(Long userId, String name, Map<String, Object> args) {
        String dateStr = (String) args.get("date");
        String date = DateParser.parseSafely(dateStr);

        AiFunction function = AiFunction.from(name);

        if (function == null) {
            return Map.of("error", "지원하지 않는 기능입니다.");
        }

        try {
            switch (function) {
                case GET_DAILY_MEAL_LOG:
                    MealDashboardResponse dashboard = mealService.getDashboard(userId, date);
                    return objectMapper.convertValue(dashboard, Map.class);

                case GET_ANALYSIS_SCORE:
                    DailyAnalysis analysis = mealService.getDailyAnalysis(userId, date);
                    if (analysis == null) {
                        try {
                            mealService.analyzeDailyMeal(userId, date);
                            analysis = mealService.getDailyAnalysis(userId, date);
                        } catch (Exception e) {
                            return Map.of("error", "분석할 식단 데이터가 부족합니다.");
                        }
                    }
                    return objectMapper.convertValue(analysis, Map.class);

                case SEARCH_FOOD_IN_DB:
                    String keyword = (String) args.get("keyword");
                    if (keyword == null || keyword.isBlank()) {
                        return Map.of("error", "검색 키워드가 필요합니다.");
                    }
                    List<FoodDetailResponse> searchResult = foodService.searchFood(keyword, 5);
                    if (searchResult.isEmpty()) {
                        return Map.of("info", "해당 키워드로 검색된 음식이 없습니다.");
                    }
                    return Map.of("search_result", searchResult);

                default:
                    return Map.of("error", "지원하지 않는 기능입니다.");
            }
        } catch (Exception e) {
            log.error("Function Execution Failed: {}", name, e);
            return Map.of("error", "데이터 조회 중 오류가 발생했습니다.");
        }
    }
}