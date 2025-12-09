package com.onlymeal.domain.user.dao;

import com.onlymeal.domain.user.dto.SignupRequest;
import com.onlymeal.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    void insertUser(SignupRequest request);
    User getUserByEmail(String email);
}
