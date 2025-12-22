package com.onlymeal.domain.meal.dao;

import com.onlymeal.domain.meal.entity.DailyAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DailyAnalysisDao {

    DailyAnalysis findByUserIdAndDate(@Param("userId") Long userId,
                                      @Param("analysisDate") String analysisDate);

    void upsertDailyAnalysis(DailyAnalysis dailyAnalysis);
}