package com.onlymeal.domain.meal.dao;

import com.onlymeal.domain.meal.dto.DailyMealStatus;
import com.onlymeal.domain.meal.entity.MealItem;
import com.onlymeal.domain.meal.entity.MealLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MealDao {
    void insertMealLog(MealLog mealLog);

    void insertMealItem(MealItem mealItem);

    boolean existsMealLog(@Param("userId") Long userId,
                          @Param("mealDate") String mealDate,
                          @Param("mealType") String mealType);

    MealLog getMealLogById(@Param("logId") Long logId);

    List<MealItem> getMealItemsByLogId(@Param("logId") Long logId);

    void updateMealType(@Param("logId") Long logId, @Param("mealType") String mealType);

    void updateMealImage(@Param("logId") Long logId, @Param("imageUrl") String imageUrl);

    void deleteMealItems(@Param("logId") Long logId);

    void deleteMealLog(@Param("logId") Long logId);

    List<MealLog> getMealLogsByDate(@Param("userId") Long userId, @Param("date") String date);

    List<DailyMealStatus> getDailyMealStatus(@Param("userId") Long userId,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate);
}