package com.onlymeal.domain.user.controller;

import com.onlymeal.domain.user.dto.UserResponse;
import com.onlymeal.domain.user.dto.UserUpdateRequest;
import com.onlymeal.domain.user.service.UserService;
import com.onlymeal.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/me")
    public ApiResponse<UserResponse> updateMyInfo(@AuthenticationPrincipal Long userId,
                                                  @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateMyInfo(userId, request);
        return ApiResponse.success(response);
    }
}