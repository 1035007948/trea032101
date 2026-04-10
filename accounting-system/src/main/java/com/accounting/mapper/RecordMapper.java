package com.accounting.mapper;

import com.accounting.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface RecordMapper {

    int insert(Record record);

    int update(Record record);

    int deleteById(Long id);

    Record selectById(Long id);

    List<Record> selectByUserId(Long userId);

    List<Record> selectByCondition(@Param("userId") Long userId,
                                   @Param("type") String type,
                                   @Param("category") String category,
                                   @Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);

    BigDecimal getTotalAmountByType(@Param("userId") Long userId,
                                    @Param("type") String type,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime);

    List<Map<String, Object>> getCategoryStats(@Param("userId") Long userId,
                                               @Param("type") String type,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    List<Map<String, Object>> getMonthlyTrend(@Param("userId") Long userId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);
}
