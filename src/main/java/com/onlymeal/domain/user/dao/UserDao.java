package com.onlymeal.domain.user.dao;

import com.onlymeal.domain.user.dto.SignupRequest;
import com.onlymeal.domain.user.dto.UserUpdateRequest;
import com.onlymeal.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    void insertUser(SignupRequest request);
    User getUserByEmail(String email);
    User getUserById(Long userId);
    void updateUser(Long userId, UserUpdateRequest request);
    void updateCoach(Long userId, String coachTone, String coachPersonality);
}
