package com.onlymeal.domain.food.service;

import com.onlymeal.domain.food.dao.FoodDao;
import com.onlymeal.domain.food.dto.FoodDetailResponse;
import com.onlymeal.domain.food.entity.Food;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodDao foodDao;

    public List<FoodDetailResponse> searchFood(String keyword, int limit) {
        if (keyword == null || keyword.isBlank()) {
            throw new CustomException(ErrorCode.KEYWORD_REQUIRED);
        }

        List<Food> foods = foodDao.searchFoods(keyword, limit);

        return foods.stream()
                .map(FoodDetailResponse::from)
                .collect(Collectors.toList());
    }
}
