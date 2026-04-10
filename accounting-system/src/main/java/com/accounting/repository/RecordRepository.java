package com.accounting.repository;

import com.accounting.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 记账记录数据访问层接口
 * 继承JpaRepository提供基本的CRUD操作
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    
    /**
     * 根据用户ID查询所有记录
     * @param userId 用户ID
     * @return 记录列表
     */
    List<Record> findByUserId(Long userId);
}
