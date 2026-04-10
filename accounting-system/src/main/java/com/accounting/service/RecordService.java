package com.accounting.service;

import com.accounting.common.BusinessException;
import com.accounting.dto.AddRecordRequest;
import com.accounting.dto.RecordQueryRequest;
import com.accounting.dto.UpdateRecordRequest;
import com.accounting.entity.Record;
import com.accounting.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 记账记录服务层
 * 处理记账记录的增删改查及统计功能
 */
@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    /**
     * 收入分类列表
     */
    public static final List<String> INCOME_CATEGORIES = Arrays.asList("薪资", "奖金", "投资收益", "其他收入");
    
    /**
     * 支出分类列表
     */
    public static final List<String> EXPENSE_CATEGORIES = Arrays.asList("餐饮", "购物", "交通", "娱乐", "医疗", "教育", "住房", "其他支出");

    /**
     * 添加记账记录
     * @param userId 用户ID
     * @param request 添加记录请求参数
     * @return 新增的记录
     */
    public Record addRecord(Long userId, AddRecordRequest request) {
        validateCategory(request.getType(), request.getCategory());
        Record record = new Record();
        record.setUserId(userId);
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setRemark(request.getRemark());
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return recordRepository.save(record);
    }

    /**
     * 更新记账记录
     * @param userId 用户ID
     * @param request 更新记录请求参数
     * @return 更新后的记录
     */
    public Record updateRecord(Long userId, UpdateRecordRequest request) {
        Record record = recordRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException("记录不存在"));
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException("无权修改此记录");
        }
        validateCategory(request.getType(), request.getCategory());
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setRemark(request.getRemark());
        record.setUpdateTime(LocalDateTime.now());
        return recordRepository.save(record);
    }

    /**
     * 删除记账记录
     * @param userId 用户ID
     * @param recordId 记录ID
     */
    public void deleteRecord(Long userId, Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException("记录不存在"));
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此记录");
        }
        recordRepository.deleteById(recordId);
    }

    /**
     * 查询记账记录列表
     * @param userId 用户ID
     * @param request 查询条件
     * @return 分页查询结果
     */
    public Map<String, Object> queryRecords(Long userId, RecordQueryRequest request) {
        List<Record> records = recordRepository.findByUserId(userId);
        
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            LocalDateTime start = LocalDate.parse(request.getStartDate()).atStartOfDay();
            records = records.stream().filter(r -> r.getCreateTime().isAfter(start) || r.getCreateTime().isEqual(start))
                    .collect(Collectors.toList());
        }
        if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            LocalDateTime end = LocalDate.parse(request.getEndDate()).atTime(LocalTime.MAX);
            records = records.stream().filter(r -> r.getCreateTime().isBefore(end) || r.getCreateTime().isEqual(end))
                    .collect(Collectors.toList());
        }
        if (request.getType() != null && !request.getType().isEmpty()) {
            records = records.stream().filter(r -> r.getType().equals(request.getType()))
                    .collect(Collectors.toList());
        }
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            records = records.stream().filter(r -> r.getCategory().equals(request.getCategory()))
                    .collect(Collectors.toList());
        }
        
        records.sort((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()));
        
        int total = records.size();
        int page = request.getPage() != null ? request.getPage() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
        if (page < 1) page = 1;
        int fromIndex = (page - 1) * pageSize;
        
        List<Record> pageRecords;
        if (fromIndex > total) {
            pageRecords = new ArrayList<>();
        } else {
            int toIndex = Math.min(fromIndex + pageSize, total);
            pageRecords = records.subList(fromIndex, toIndex);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageRecords);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * 验证分类是否有效
     * @param type 类型（收入/支出）
     * @param category 分类名称
     */
    private void validateCategory(String type, String category) {
        if ("收入".equals(type)) {
            if (!INCOME_CATEGORIES.contains(category)) {
                throw new BusinessException("收入分类不正确，可选值：" + INCOME_CATEGORIES);
            }
        } else if ("支出".equals(type)) {
            if (!EXPENSE_CATEGORIES.contains(category)) {
                throw new BusinessException("支出分类不正确，可选值：" + EXPENSE_CATEGORIES);
            }
        }
    }

    /**
     * 获取本周统计数据
     * @param userId 用户ID
     * @return 统计结果
     */
    public Map<String, Object> getWeeklyStats(Long userId) {
        List<Record> records = recordRepository.findByUserId(userId);
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.CHINA).dayOfWeek(), 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        
        return calculateStats(records, startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX));
    }

    /**
     * 获取本月统计数据
     * @param userId 用户ID
     * @return 统计结果
     */
    public Map<String, Object> getMonthlyStats(Long userId) {
        List<Record> records = recordRepository.findByUserId(userId);
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        
        return calculateStats(records, startOfMonth.atStartOfDay(), endOfMonth.atTime(LocalTime.MAX));
    }

    /**
     * 计算统计数据
     * @param records 记录列表
     * @param start 开始时间
     * @param end 结束时间
     * @return 统计结果
     */
    private Map<String, Object> calculateStats(List<Record> records, LocalDateTime start, LocalDateTime end) {
        List<Record> filteredRecords = records.stream()
                .filter(r -> (r.getCreateTime().isAfter(start) || r.getCreateTime().isEqual(start)) &&
                            (r.getCreateTime().isBefore(end) || r.getCreateTime().isEqual(end)))
                .collect(Collectors.toList());
        
        double totalIncome = filteredRecords.stream()
                .filter(r -> "支出".equals(r.getType()))
                .mapToDouble(r -> r.getAmount().doubleValue())
                .sum();
        
        double totalExpense = filteredRecords.stream()
                .filter(r -> "收入".equals(r.getType()))
                .mapToDouble(r -> r.getAmount().doubleValue())
                .sum();
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("balance", totalIncome - totalExpense);
        return result;
    }

    /**
     * 获取分类统计数据
     * @param userId 用户ID
     * @param type 类型（收入/支出），为空则统计全部
     * @return 分类统计结果
     */
    public Map<String, Object> getCategoryStats(Long userId, String type) {
        List<Record> records = recordRepository.findByUserId(userId);
        
        Map<String, Double> categoryTotals = records.stream()
                .filter(r -> type == null || type.isEmpty() || r.getType().equals(type))
                .collect(Collectors.groupingBy(Record::getCategory, 
                        Collectors.summingDouble(r -> r.getAmount().doubleValue())));
        
        double total = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        
        List<Map<String, Object>> categoryList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("category", entry.getKey());
            item.put("amount", entry.getValue());
            item.put("percentage", total > 0 ? String.format("%.2f", entry.getValue() / total * 100) : "0.00");
            categoryList.add(item);
        }
        
        categoryList.sort((a, b) -> Double.compare((Double) b.get("amount"), (Double) a.get("amount")));
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", categoryList);
        result.put("total", total);
        return result;
    }
}
