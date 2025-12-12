package com.onlymeal.domain.meal.entity;

import com.onlymeal.domain.meal.dto.MealCreateRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealLog {
    private Long logId;
    private Long userId;
    private String mealType;
    private String mealDate;
    private String imageUrl;

    private List<MealItem> mealItems;

    public static MealLog create(Long userId, MealCreateRequest request, String imageUrl) {
        return MealLog.builder()
                .userId(userId)
                .mealType(request.getMealType())
                .mealDate(request.getMealDate())
                .imageUrl(imageUrl)
                .build();
    }
}