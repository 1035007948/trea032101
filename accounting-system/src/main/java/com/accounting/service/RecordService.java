package com.accounting.service;

import com.accounting.common.BusinessException;
import com.accounting.dto.AddRecordRequest;
import com.accounting.dto.RecordQueryRequest;
import com.accounting.dto.UpdateRecordRequest;
import com.accounting.entity.Record;
import com.accounting.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 收支记录服务层
 * 处理收支记录相关的业务逻辑
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
     * 添加收支记录
     *
     * @param userId  用户ID
     * @param request 添加记录请求对象
     * @return 保存后的记录对象
     * @throws BusinessException 当分类不正确时抛出
     */
    public Record addRecord(Long userId, AddRecordRequest request) {
        // 验证分类
        validateCategory(request.getType(), request.getCategory());
        // 创建记录
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
     *
     * @param userId  用户ID
     * @param request 更新记录请求对象
     * @return 更新后的记录对象
     * @throws BusinessException 当记录不存在、无权修改或分类不正确时抛出
     */
    public Record updateRecord(Long userId, UpdateRecordRequest request) {
        // 查询记录
        Record record = recordRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException("记录不存在"));
        // 验证权限
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException("无权修改此记录");
        }
        // 验证分类
        validateCategory(request.getType(), request.getCategory());
        // 更新记录
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setRemark(request.getRemark());
        return recordRepository.save(record);
    }

    /**
     * 删除收支记录
     *
     * @param userId   用户ID
     * @param recordId 记录ID
     * @throws BusinessException 当记录不存在或无权删除时抛出
     */
    public void deleteRecord(Long userId, Long recordId) {
        // 查询记录
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException("记录不存在"));
        // 验证权限
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此记录");
        }
        recordRepository.deleteById(recordId);
    }

    /**
     * 查询收支记录列表（支持分页和筛选）
     *
     * @param userId  用户ID
     * @param request 查询请求对象
     * @return 包含记录列表和分页信息的Map
     */
    public Map<String, Object> queryRecords(Long userId, RecordQueryRequest request) {
        // 构建排序规则
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        // 构建分页对象
        int page = request.getPage() != null && request.getPage() > 0 ? request.getPage() : 1;
        int pageSize = request.getPageSize() != null && request.getPageSize() > 0 ? request.getPageSize() : 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        // 查询数据
        Page<Record> recordPage;
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()
                && request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            // 按时间范围查询
            LocalDateTime start = LocalDate.parse(request.getStartDate()).atStartOfDay();
            LocalDateTime end = LocalDate.parse(request.getEndDate()).atTime(LocalTime.MAX);
            recordPage = recordRepository.findByUserIdAndCreateTimeBetween(userId, start, end, pageable);
        } else if (request.getType() != null && !request.getType().isEmpty()) {
            // 按类型查询
            recordPage = recordRepository.findByUserIdAndType(userId, request.getType(), pageable);
        } else if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            // 按分类查询
            recordPage = recordRepository.findByUserIdAndCategory(userId, request.getCategory(), pageable);
        } else {
            // 查询所有
            recordPage = recordRepository.findByUserId(userId, pageable);
        }

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", recordPage.getContent());
        result.put("total", recordPage.getTotalElements());
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", recordPage.getTotalPages());
        return result;
    }

    /**
     * 验证分类是否合法
     *
     * @param type     类型（收入/支出）
     * @param category 分类
     * @throws BusinessException 当分类不正确时抛出
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
     * 获取本周统计
     *
     * @param userId 用户ID
     * @return 统计信息Map
     */
    public Map<String, Object> getWeeklyStats(Long userId) {
        // 获取本周开始和结束时间
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.CHINA).dayOfWeek(), 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        // 查询本周记录
        List<Record> records = recordRepository.findByUserIdAndCreateTimeBetween(
                userId, startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX));
        return calculateStats(records);
    }

    /**
     * 获取本月统计
     *
     * @param userId 用户ID
     * @return 统计信息Map
     */
    public Map<String, Object> getMonthlyStats(Long userId) {
        // 获取本月开始和结束时间
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        // 查询本月记录
        List<Record> records = recordRepository.findByUserIdAndCreateTimeBetween(
                userId, startOfMonth.atStartOfDay(), endOfMonth.atTime(LocalTime.MAX));
        return calculateStats(records);
    }

    /**
     * 计算统计数据
     *
     * @param records 记录列表
     * @return 统计信息Map
     */
    private Map<String, Object> calculateStats(List<Record> records) {
        // 计算总收入
        double totalIncome = records.stream()
                .filter(r -> "收入".equals(r.getType()))
                .mapToDouble(r -> r.getAmount().doubleValue())
                .sum();
        // 计算总支出
        double totalExpense = records.stream()
                .filter(r -> "支出".equals(r.getType()))
                .mapToDouble(r -> r.getAmount().doubleValue())
                .sum();
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("balance", totalIncome - totalExpense);
        return result;
    }

    /**
     * 获取分类统计
     *
     * @param userId 用户ID
     * @param type   类型筛选（可选）
     * @return 分类统计信息Map
     */
    public Map<String, Object> getCategoryStats(Long userId, String type) {
        // 查询用户所有记录
        List<Record> records = recordRepository.findByUserId(userId);
        // 按分类分组统计
        Map<String, Double> categoryTotals = records.stream()
                .filter(r -> type == null || type.isEmpty() || r.getType().equals(type))
                .collect(Collectors.groupingBy(Record::getCategory,
                        Collectors.summingDouble(r -> r.getAmount().doubleValue())));
        // 计算总额
        double total = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        // 构建分类列表
        List<Map<String, Object>> categoryList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("category", entry.getKey());
            item.put("amount", entry.getValue());
            item.put("percentage", total > 0 ? String.format("%.2f", entry.getValue() / total * 100) : "0.00");
            categoryList.add(item);
        }
        // 按金额降序排序
        categoryList.sort((a, b) -> Double.compare((Double) b.get("amount"), (Double) a.get("amount")));
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", categoryList);
        result.put("total", total);
        return result;
    }
}
