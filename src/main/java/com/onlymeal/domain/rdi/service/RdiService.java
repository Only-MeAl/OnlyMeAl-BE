package com.onlymeal.domain.rdi.service;

import com.onlymeal.domain.rdi.dao.RdiDao;
import com.onlymeal.domain.rdi.dto.RdiResponse;
import com.onlymeal.domain.rdi.entity.RdiStandard;
import com.onlymeal.domain.user.dao.UserDao;
import com.onlymeal.domain.user.entity.User;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class RdiService {

    private final RdiDao rdiDao;
    private final UserDao userDao;

    public RdiResponse getRdi(Long userId) {
        User user = userDao.getUserById(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        int age = calculateAge(user.getBirthDate());
        RdiStandard rdiStandard = rdiDao.getRdiByGenderAndAge(user.getGender(), age);

        if (rdiStandard == null) {
            throw new CustomException(ErrorCode.RDI_NOT_FOUND);
        }

        double activityMultiplier = getActivityMultiplier(user.getActivityLevel());
        double goalMultiplier = getGoalMultiplier(user.getTargetGoal());
        double totalMultiplier = activityMultiplier * goalMultiplier;

        return RdiResponse.from(rdiStandard, totalMultiplier);
    }

    private int calculateAge(String birthDate) {
        LocalDate birth = LocalDate.parse(birthDate);
        LocalDate now = LocalDate.now();
        return Period.between(birth, now).getYears();
    }

    private double getActivityMultiplier(String activityLevel) {
        return switch (activityLevel) {
            case "SEDENTARY" -> 0.8;
            case "LOW" -> 0.9;
            case "MODERATE" -> 1.0;
            case "ACTIVE" -> 1.1;
            case "VERY_ACTIVE" -> 1.2;
            default -> 1.0;
        };
    }

    private double getGoalMultiplier(String targetGoal) {
        return switch (targetGoal) {
            case "DIET" -> 0.85;
            case "MAINTAIN" -> 1.0;
            case "BULK_UP" -> 1.15;
            default -> 1.0;
        };
    }
}