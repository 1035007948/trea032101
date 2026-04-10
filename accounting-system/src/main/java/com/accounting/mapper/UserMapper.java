package com.accounting.mapper;

import com.accounting.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    int update(User user);

    int deleteById(Long id);

    User selectById(Long id);

    User selectByAccount(String account);

    List<User> selectAll();

    int updatePassword(@Param("id") Long id, @Param("newPassword") String newPassword);
}
