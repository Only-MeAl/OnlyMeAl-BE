package com.onlymeal.domain.rdi.controller;

import com.onlymeal.domain.rdi.dto.RdiResponse;
import com.onlymeal.domain.rdi.service.RdiService;
import com.onlymeal.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class RdiController {

    private final RdiService rdiService;

    @GetMapping("/rdi")
    public ApiResponse<RdiResponse> getRdi(@AuthenticationPrincipal Long userId) {
        RdiResponse response = rdiService.getRdi(userId);
        return ApiResponse.success(response);
    }
}