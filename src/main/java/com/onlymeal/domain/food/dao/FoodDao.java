package com.onlymeal.domain.food.dao;

import com.onlymeal.domain.food.entity.Food;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FoodDao {
    List<Food> searchFoods(@Param("keyword") String keyword, @Param("limit") int limit);
    Food getFoodById(@Param("foodId") Long foodId);

    List<Food> getAllFoods();
}