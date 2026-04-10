package com.accounting.service;

import com.accounting.common.BusinessException;
import com.accounting.dto.AddRecordRequest;
import com.accounting.dto.RecordQueryRequest;
import com.accounting.dto.UpdateRecordRequest;
import com.accounting.entity.Record;
import com.accounting.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 收支记录服务类
 * 处理记录的增删改查、统计分析等业务逻辑
 */
@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public static final List<String> INCOME_CATEGORIES = Arrays.asList("薪资", "奖金", "投资收益", "其他收入");
    public static final List<String> EXPENSE_CATEGORIES = Arrays.asList("餐饮", "购物", "交通", "娱乐", "医疗", "教育", "住房", "其他支出");

    /**
     * 添加收支记录
     * @param userId 用户ID
     * @param request 添加记录请求参数
     * @return 新增的记录实体
     * @throws BusinessException 分类不合法时抛出异常
     */
    public Record addRecord(Long userId, AddRecordRequest request) {
        validateCategory(request.getType(), request.getCategory());
        Record record = new Record();
        record.setUserId(userId);
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setRemark(request.getRemark());
        return recordRepository.save(record);
    }

    /**
     * 更新收支记录
     * @param userId 用户ID
     * @param request 更新记录请求参数
     * @return 更新后的记录实体
     * @throws BusinessException 记录不存在或无权限时抛出异常
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
        return recordRepository.save(record);
    }

    /**
     * 删除收支记录
     * @param userId 用户ID
     * @param recordId 记录ID
     * @throws BusinessException 记录不存在或无权限时抛出异常
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
     * 分页查询收支记录（使用数据库条件查询）
     * @param userId 用户ID
     * @param request 查询条件
     * @return 分页结果，包含列表、总数、页码、页大小
     */
    public Map<String, Object> queryRecords(Long userId, RecordQueryRequest request) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            startTime = LocalDate.parse(request.getStartDate()).atStartOfDay();
        }
        if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            endTime = LocalDate.parse(request.getEndDate()).atTime(LocalTime.MAX);
        }

        List<Record> records = recordRepository.findByCondition(
                userId, request.getType(), request.getCategory(), startTime, endTime);

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
     * 验证分类是否合法
     * @param type 记录类型（收入/支出）
     * @param category 分类名称
     * @throws BusinessException 分类不合法时抛出异常
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
     * @return 统计结果，包含总收入、总支出、结余
     */
    public Map<String, Object> getWeeklyStats(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.CHINA).dayOfWeek(), 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return calculateStats(userId, startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX));
    }

    /**
     * 获取本月统计数据
     * @param userId 用户ID
     * @return 统计结果，包含总收入、总支出、结余
     */
    public Map<String, Object> getMonthlyStats(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return calculateStats(userId, startOfMonth.atStartOfDay(), endOfMonth.atTime(LocalTime.MAX));
    }

    /**
     * 计算指定时间范围内的统计数据（使用数据库查询）
     * @param userId 用户ID
     * @param start 开始时间
     * @param end 结束时间
     * @return 统计结果
     */
    private Map<String, Object> calculateStats(Long userId, LocalDateTime start, LocalDateTime end) {
        BigDecimal totalIncome = recordRepository.getTotalAmountByType(userId, "收入", start, end);
        BigDecimal totalExpense = recordRepository.getTotalAmountByType(userId, "支出", start, end);

        Map<String, Object> result = new HashMap<>();
        result.put("totalIncome", totalIncome.doubleValue());
        result.put("totalExpense", totalExpense.doubleValue());
        result.put("balance", totalIncome.subtract(totalExpense).doubleValue());
        return result;
    }

    /**
     * 获取分类统计数据（使用数据库聚合查询）
     * @param userId 用户ID
     * @param type 记录类型（可选，null表示所有类型）
     * @return 分类统计结果，包含各分类金额及占比
     */
    public Map<String, Object> getCategoryStats(Long userId, String type) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        List<Map<String, Object>> incomeStats = Collections.emptyList();
        List<Map<String, Object>> expenseStats = Collections.emptyList();

        if (type == null || type.isEmpty() || "收入".equals(type)) {
            incomeStats = recordRepository.getCategoryStats(userId, "收入", startTime, endTime);
        }
        if (type == null || type.isEmpty() || "支出".equals(type)) {
            expenseStats = recordRepository.getCategoryStats(userId, "支出", startTime, endTime);
        }

        BigDecimal incomeTotal = recordRepository.getTotalAmountByType(userId, "收入", startTime, endTime);
        BigDecimal expenseTotal = recordRepository.getTotalAmountByType(userId, "支出", startTime, endTime);

        List<Map<String, Object>> allStats = new ArrayList<>();
        allStats.addAll(calculatePercentage(incomeStats, incomeTotal));
        allStats.addAll(calculatePercentage(expenseStats, expenseTotal));

        allStats.sort((a, b) -> Double.compare(
                ((Number) b.get("total")).doubleValue(),
                ((Number) a.get("total")).doubleValue()));

        Map<String, Object> result = new HashMap<>();
        result.put("list", allStats);
        result.put("totalIncome", incomeTotal.doubleValue());
        result.put("totalExpense", expenseTotal.doubleValue());
        return result;
    }

    /**
     * 计算分类占比
     * @param stats 分类统计列表
     * @param total 总金额
     * @return 添加占比后的统计列表
     */
    private List<Map<String, Object>> calculatePercentage(List<Map<String, Object>> stats, BigDecimal total) {
        return stats.stream().peek(map -> {
            BigDecimal amount = new BigDecimal(map.get("total").toString());
            String percentage = total.compareTo(BigDecimal.ZERO) > 0
                    ? String.format("%.2f", amount.divide(total, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")))
                    : "0.00";
            map.put("percentage", percentage);
        }).collect(Collectors.toList());
    }
}
