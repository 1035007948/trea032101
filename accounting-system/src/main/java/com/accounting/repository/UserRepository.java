package com.accounting.repository;

import com.accounting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 * 继承JpaRepository，提供基础的CRUD操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据账号查询用户
     *
     * @param account 账号
     * @return 用户Optional对象
     */
    Optional<User> findByAccount(String account);

    /**
     * 检查账号是否已存在
     *
     * @param account 账号
     * @return 是否存在
     */
    boolean existsByAccount(String account);
}
