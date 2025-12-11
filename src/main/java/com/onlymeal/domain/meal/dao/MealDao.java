package com.onlymeal.domain.meal.dao;

import com.onlymeal.domain.meal.entity.MealItem;
import com.onlymeal.domain.meal.entity.MealLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MealDao {
    void insertMealLog(MealLog mealLog);

    void insertMealItem(MealItem mealItem);

    boolean existsMealLog(@Param("userId") Long userId,
                          @Param("mealDate") String mealDate,
                          @Param("mealType") String mealType);
}