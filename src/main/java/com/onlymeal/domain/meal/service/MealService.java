package com.onlymeal.domain.meal.service;

import com.onlymeal.domain.food.dao.FoodDao;
import com.onlymeal.domain.meal.dao.DailyAnalysisDao;
import com.onlymeal.domain.meal.dao.MealDao;
import com.onlymeal.domain.meal.dto.*;
import com.onlymeal.domain.meal.entity.DailyAnalysis;
import com.onlymeal.domain.meal.entity.MealItem;
import com.onlymeal.domain.meal.entity.MealLog;
import com.onlymeal.domain.rdi.dto.RdiResponse;
import com.onlymeal.domain.rdi.service.RdiService;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import com.onlymeal.global.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealDao mealDao;
    private final FoodDao foodDao;
    private final FileStorage fileStorage;
    private final RdiService rdiService;
    private final DailyAnalysisDao dailyAnalysisDao;

    @Transactional
    public void createMeal(Long userId, MealCreateRequest request, MultipartFile image) {
        if (mealDao.existsMealLog(userId, request.getMealDate(), request.getMealType())) {
            throw new CustomException(ErrorCode.DUPLICATE_MEAL);
        }

        String imageUrl = fileStorage.store(image, userId, request.getMealType());

        validateFoodIds(request.getItems());

        MealLog mealLog = MealLog.create(userId, request, imageUrl);
        mealDao.insertMealLog(mealLog);

        List<MealItem> mealItems = MealItem.createList(mealLog.getLogId(), request.getItems());

        for (MealItem item : mealItems) {
            mealDao.insertMealItem(item);
        }
    }

    public MealDetailResponse getMealDetail(Long logId, Long userId) {
        MealLog mealLog = mealDao.getMealLogById(logId);

        if (mealLog == null) {
            throw new CustomException(ErrorCode.MEAL_NOT_FOUND);
        }

        if (!mealLog.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<MealItem> mealItems = mealDao.getMealItemsByLogId(logId);

        return MealDetailResponse.of(mealLog, mealItems);
    }

    @Transactional
    public void updateMeal(Long logId, Long userId, MealUpdateRequest request, MultipartFile image) {
        MealLog mealLog = validateAndGetMealLog(logId, userId);

        if (image != null && !image.isEmpty()) {
            updateImage(mealLog, request, image);
        }

        if (request != null) {
            updateMealData(mealLog, request);
        }
    }

    @Transactional
    public void deleteMeal(Long logId, Long userId) {
        MealLog mealLog = mealDao.getMealLogById(logId);

        if (mealLog == null) {
            throw new CustomException(ErrorCode.MEAL_NOT_FOUND);
        }

        if (!mealLog.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        if (mealLog.getImageUrl() != null) {
            fileStorage.delete(mealLog.getImageUrl());
        }

        mealDao.deleteMealLog(logId);
    }

    public MealDashboardResponse getDashboard(Long userId, String date) {
        List<MealLog> mealLogs = mealDao.getMealLogsByDate(userId, date);
        List<MealDetailResponse> meals = new ArrayList<>();

        NutrientAccumulator accumulator = new NutrientAccumulator();

        for (MealLog log : mealLogs) {
            List<MealItem> items = log.getMealItems();
            if (items == null) items = new ArrayList<>();

            meals.add(MealDetailResponse.of(log, items));

            for (MealItem item : items) {
                accumulator.add(item);
            }
        }

        RdiResponse rdi = rdiService.getRdi(userId);

        DailySummary dailySummary = accumulator.toDailySummary(rdi);

        return new MealDashboardResponse(meals, dailySummary);
    }

    public List<DailyMealStatus> getDailyMealStatus(Long userId, String startDate, String endDate) {
        return mealDao.getDailyMealStatus(userId, startDate, endDate);
    }

    private void validateFoodIds(List<MealItemRequest> items) {
        for (MealItemRequest item : items) {
            if (foodDao.getFoodById(item.getFoodId()) == null) {
                throw new CustomException(ErrorCode.FOOD_NOT_FOUND);
            }
        }
    }

    private MealLog validateAndGetMealLog(Long logId, Long userId) {
        MealLog mealLog = mealDao.getMealLogById(logId);
        if (mealLog == null) {
            throw new CustomException(ErrorCode.MEAL_NOT_FOUND);
        }
        if (!mealLog.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        return mealLog;
    }

    private void updateImage(MealLog mealLog, MealUpdateRequest request, MultipartFile image) {
        fileStorage.delete(mealLog.getImageUrl());

        String currentMealType = (request != null && request.getMealType() != null)
                ? request.getMealType()
                : mealLog.getMealType();

        String newImageUrl = fileStorage.store(image, mealLog.getUserId(), currentMealType);
        mealDao.updateMealImage(mealLog.getLogId(), newImageUrl);
    }

    private void updateMealData(MealLog mealLog, MealUpdateRequest request) {
        if (request.getMealType() != null && !request.getMealType().equals(mealLog.getMealType())) {
            if (mealDao.existsMealLog(mealLog.getUserId(), mealLog.getMealDate(), request.getMealType())) {
                throw new CustomException(ErrorCode.DUPLICATE_MEAL);
            }
            mealDao.updateMealType(mealLog.getLogId(), request.getMealType());
        }

        if (request.getItems() != null) {
            validateFoodIds(request.getItems());

            mealDao.deleteMealItems(mealLog.getLogId());

            List<MealItem> mealItems = MealItem.createList(mealLog.getLogId(), request.getItems());
            for (MealItem item : mealItems) {
                mealDao.insertMealItem(item);
            }
        }
    }

    public DailyAnalysis getDailyAnalysis(Long userId, String date) {
        return dailyAnalysisDao.findByUserIdAndDate(userId, date);
    }

    @Transactional
    public DailyAnalysisResponse analyzeDailyMeal(Long userId, String date) {
        List<MealLog> mealLogs = mealDao.getMealLogsByDate(userId, date);

        if (mealLogs.isEmpty()) {
            throw new CustomException(ErrorCode.NO_MEAL_FOR_DATE);
        }

        RdiResponse rdi = rdiService.getRdi(userId);

        NutrientAccumulator accumulator = new NutrientAccumulator();
        for (MealLog log : mealLogs) {
            for (MealItem item : log.getMealItems()) {
                accumulator.add(item);
            }
        }

        DailyAnalysis analysis = accumulator.toDailyAnalysis(userId, LocalDate.parse(date), rdi);
        dailyAnalysisDao.upsertDailyAnalysis(analysis);

        return DailyAnalysisResponse.of(analysis, rdi);
    }
}