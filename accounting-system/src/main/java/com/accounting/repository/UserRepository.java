package com.accounting.repository;

import com.accounting.entity.User;
import com.accounting.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @Resource
    private UserMapper userMapper;

    public List<User> findAll() {
        return userMapper.selectAll();
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id));
    }

    public Optional<User> findByAccount(String account) {
        return Optional.ofNullable(userMapper.selectByAccount(account));
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            user.setUpdateTime(LocalDateTime.now());
            userMapper.update(user);
        }
        return user;
    }

    public void updatePassword(Long userId, String newPassword) {
        userMapper.updatePassword(userId, newPassword);
    }
}
