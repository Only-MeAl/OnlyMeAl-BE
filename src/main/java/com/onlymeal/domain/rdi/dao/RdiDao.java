package com.onlymeal.domain.rdi.dao;

import com.onlymeal.domain.rdi.entity.RdiStandard;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RdiDao {
    RdiStandard getRdiByGenderAndAge(String gender, int age);
}