package com.onlymeal.domain.user.service;

import com.onlymeal.domain.user.dao.UserDao;
import com.onlymeal.domain.user.dto.SignupRequest;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest request) {
        if (userDao.getUserByEmail(request.getEmail()) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userDao.insertUser(request);
    }
}