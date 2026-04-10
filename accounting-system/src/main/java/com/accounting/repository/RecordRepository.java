package com.accounting.repository;

import com.accounting.entity.Record;
import com.accounting.mapper.RecordMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RecordRepository {

    @Resource
    private RecordMapper recordMapper;

    public List<Record> findAll() {
        throw new UnsupportedOperationException("不支持查询所有记录，请使用findByUserId");
    }

    public List<Record> findByUserId(Long userId) {
        return recordMapper.selectByUserId(userId);
    }

    public List<Record> findByCondition(Long userId, String type, String category,
                                         LocalDateTime startTime, LocalDateTime endTime) {
        return recordMapper.selectByCondition(userId, type, category, startTime, endTime);
    }

    public Optional<Record> findById(Long id) {
        return Optional.ofNullable(recordMapper.selectById(id));
    }

    public Record save(Record record) {
        if (record.getId() == null) {
            record.setCreateTime(LocalDateTime.now());
            recordMapper.insert(record);
        } else {
            record.setUpdateTime(LocalDateTime.now());
            recordMapper.update(record);
        }
        return record;
    }

    public void deleteById(Long id) {
        recordMapper.deleteById(id);
    }

    public BigDecimal getTotalAmountByType(Long userId, String type,
                                           LocalDateTime startTime, LocalDateTime endTime) {
        return recordMapper.getTotalAmountByType(userId, type, startTime, endTime);
    }

    public List<Map<String, Object>> getCategoryStats(Long userId, String type,
                                                      LocalDateTime startTime, LocalDateTime endTime) {
        return recordMapper.getCategoryStats(userId, type, startTime, endTime);
    }

    public List<Map<String, Object>> getMonthlyTrend(Long userId,
                                                      LocalDateTime startTime, LocalDateTime endTime) {
        return recordMapper.getMonthlyTrend(userId, startTime, endTime);
    }
}
