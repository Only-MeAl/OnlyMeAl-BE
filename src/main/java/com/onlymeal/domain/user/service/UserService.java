package com.onlymeal.domain.user.service;

import com.onlymeal.domain.user.dao.UserDao;
import com.onlymeal.domain.user.dto.LoginRequest;
import com.onlymeal.domain.user.dto.LoginResponse;
import com.onlymeal.domain.user.dto.SignupRequest;
import com.onlymeal.domain.user.dto.UserResponse;
import com.onlymeal.domain.user.entity.User;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import com.onlymeal.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signup(SignupRequest request) {
        if (userDao.getUserByEmail(request.getEmail()) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userDao.insertUser(request);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userDao.getUserByEmail(request.getEmail());

        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtTokenProvider.createToken(user.getUserId());
        return new LoginResponse(token, user.getNickname());
    }

    public UserResponse getMyInfo(Long userId) {
        User user = userDao.getUserById(userId);

        if(user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return UserResponse.from(user);
    }
}