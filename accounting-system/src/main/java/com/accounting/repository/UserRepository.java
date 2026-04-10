package com.accounting.repository;

import com.accounting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层接口
 * 继承JpaRepository提供基本的CRUD操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据账号查询用户
     * @param account 用户账号
     * @return 用户对象
     */
    Optional<User> findByAccount(String account);
}
