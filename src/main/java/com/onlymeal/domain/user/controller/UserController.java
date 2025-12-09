package com.onlymeal.domain.user.controller;

import com.onlymeal.domain.user.dto.UserResponse;
import com.onlymeal.domain.user.service.UserService;
import com.onlymeal.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyInfo(@AuthenticationPrincipal Long userId) {
        UserResponse response = userService.getMyInfo(userId);
        return ApiResponse.success(response);
    }
}