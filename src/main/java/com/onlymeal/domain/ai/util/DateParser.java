package com.onlymeal.domain.ai.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class DateParser {

    private DateParser() {}

    public static String parseSafely(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return LocalDate.now().toString();
        }

        try {
            LocalDate.parse(dateStr);
            return dateStr;
        } catch (Exception ignored) {}

        String normalized = dateStr.trim().toLowerCase();
        LocalDate today = LocalDate.now();

        switch (normalized) {
            case "오늘":
            case "today":
                return today.toString();
            case "어제":
            case "yesterday":
                return today.minusDays(1).toString();
            case "내일":
            case "tomorrow":
                return today.plusDays(1).toString();
            case "그저께":
            case "엊그제":
                return today.minusDays(2).toString();
            case "모레":
                return today.plusDays(2).toString();
        }

        if (normalized.matches("\\d+일\\s*전")) {
            int days = Integer.parseInt(normalized.replaceAll("[^0-9]", ""));
            return today.minusDays(days).toString();
        }

        if (normalized.contains("지난주") || normalized.contains("저번주")) {
            return today.minusWeeks(1).toString();
        }

        log.warn("날짜 파싱 실패, 기본값(오늘) 사용: {}", dateStr);
        return today.toString();
    }
}