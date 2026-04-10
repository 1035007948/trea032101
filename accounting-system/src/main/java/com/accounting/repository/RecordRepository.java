package com.accounting.repository;

import com.accounting.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收支记录数据访问层
 * 继承JpaRepository，提供基础的CRUD操作
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    /**
     * 根据用户ID查询所有记录
     *
     * @param userId 用户ID
     * @return 记录列表
     */
    List<Record> findByUserId(Long userId);

    /**
     * 根据用户ID分页查询记录
     *
     * @param userId   用户ID
     * @param pageable 分页对象
     * @return 分页记录
     */
    Page<Record> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据用户ID和创建时间范围查询记录
     *
     * @param userId    用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 记录列表
     */
    List<Record> findByUserIdAndCreateTimeBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户ID和创建时间范围分页查询记录
     *
     * @param userId    用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页对象
     * @return 分页记录
     */
    Page<Record> findByUserIdAndCreateTimeBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据用户ID和类型查询记录
     *
     * @param userId 用户ID
     * @param type   类型（收入/支出）
     * @return 记录列表
     */
    List<Record> findByUserIdAndType(Long userId, String type);

    /**
     * 根据用户ID和类型分页查询记录
     *
     * @param userId   用户ID
     * @param type     类型（收入/支出）
     * @param pageable 分页对象
     * @return 分页记录
     */
    Page<Record> findByUserIdAndType(Long userId, String type, Pageable pageable);

    /**
     * 根据用户ID和分类查询记录
     *
     * @param userId   用户ID
     * @param category 分类
     * @return 记录列表
     */
    List<Record> findByUserIdAndCategory(Long userId, String category);

    /**
     * 根据用户ID和分类分页查询记录
     *
     * @param userId   用户ID
     * @param category 分类
     * @param pageable 分页对象
     * @return 分页记录
     */
    Page<Record> findByUserIdAndCategory(Long userId, String category, Pageable pageable);

    /**
     * 根据用户ID查询记录并按创建时间降序排序
     *
     * @param userId 用户ID
     * @return 记录列表
     */
    List<Record> findByUserIdOrderByCreateTimeDesc(Long userId);
}
