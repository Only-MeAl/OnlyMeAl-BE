package com.onlymeal.domain.ai.util;

import com.onlymeal.domain.rdi.dto.RdiResponse;
import com.onlymeal.domain.user.entity.User;
import com.onlymeal.domain.user.type.CoachPersonality;
import com.onlymeal.domain.user.type.CoachTone;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PromptBuilder {

    @Value("${ai.prompts.chat-system}")
    private Resource chatSystemPromptResource;

    private String chatSystemPromptTemplate;

    @PostConstruct
    public void init() {
        try {
            this.chatSystemPromptTemplate = StreamUtils.copyToString(
                    chatSystemPromptResource.getInputStream(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            log.error("채팅 프롬프트 로드 실패", e);
            throw new CustomException(ErrorCode.PROMPT_LOAD_FAILED);
        }
    }

    public String buildChatSystemPrompt(User user, RdiResponse rdi) {
        String prompt = chatSystemPromptTemplate;

        prompt = prompt.replace("{nickname}", user.getNickname());
        prompt = prompt.replace("{target_goal}", user.getTargetGoal());
        prompt = prompt.replace("{current_date}", LocalDate.now().toString());
        prompt = prompt.replace("{tone_description}",
                CoachTone.resolveDescription(user.getCoachTone()));
        prompt = prompt.replace("{personality_description}",
                CoachPersonality.resolveDescription(user.getCoachPersonality()));

        prompt = prompt.replace("{rec_calories}", String.valueOf(rdi.getRecCalories().intValue()));
        prompt = prompt.replace("{rec_carbs}", String.valueOf(rdi.getRecCarbs().intValue()));
        prompt = prompt.replace("{rec_protein}", String.valueOf(rdi.getRecProtein().intValue()));
        prompt = prompt.replace("{rec_fat}", String.valueOf(rdi.getRecFat().intValue()));
        prompt = prompt.replace("{rec_sugars}", String.valueOf(rdi.getRecSugars().intValue()));
        prompt = prompt.replace("{rec_sodium}", String.valueOf(rdi.getRecSodium().intValue()));

        return prompt;
    }
}