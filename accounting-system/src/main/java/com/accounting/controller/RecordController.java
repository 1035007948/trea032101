package com.accounting.controller;

import com.accounting.common.BusinessException;
import com.accounting.common.Result;
import com.accounting.dto.AddRecordRequest;
import com.accounting.dto.RecordQueryRequest;
import com.accounting.dto.UpdateRecordRequest;
import com.accounting.entity.Record;
import com.accounting.service.RecordService;
import com.accounting.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记账记录控制器
 * 处理记账记录的增删改查请求
 */
@Api(tags = "记账记录管理")
@RestController
@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private UserService userService;

    /**
     * 根据token获取用户ID
     * @param token 用户token
     * @return 用户ID
     */
    private Long getUserId(String token) {
        Long userId = userService.getUserIdByToken(token);
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }

    /**
     * 添加记账记录
     * @param token 用户token
     * @param request 添加记录请求参数
     * @return 新增的记录
     */
    @ApiOperation("添加记账记录")
    @PostMapping("/add")
    public Result<Record> addRecord(
            @ApiParam(value = "用户token", required = true) @RequestHeader("Authorization") String token,
            @Valid @RequestBody AddRecordRequest request) {
        Long userId = getUserId(token);
        Record record = recordService.addRecord(userId, request);
        return Result.success(record);
    }

    /**
     * 更新记账记录
     * @param token 用户token
     * @param request 更新记录请求参数
     * @return 更新后的记录
     */
    @ApiOperation("更新记账记录")
    @PostMapping("/update")
    public Result<Record> updateRecord(
            @ApiParam(value = "用户token", required = true) @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateRecordRequest request) {
        Long userId = getUserId(token);
        Record record = recordService.updateRecord(userId, request);
        return Result.success(record);
    }

    /**
     * 删除记账记录
     * @param token 用户token
     * @param id 记录ID
     * @return 操作结果
     */
    @ApiOperation("删除记账记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRecord(
            @ApiParam(value = "用户token", required = true) @RequestHeader("Authorization") String token,
            @ApiParam(value = "记录ID", required = true) @PathVariable Long id) {
        Long userId = getUserId(token);
        recordService.deleteRecord(userId, id);
        return Result.success();
    }

    /**
     * 查询记账记录列表
     * @param token 用户token
     * @param request 查询条件
     * @return 分页查询结果
     */
    @ApiOperation("查询记账记录列表")
    @GetMapping("/list")
    public Result<Map<String, Object>> queryRecords(
            @ApiParam(value = "用户token", required = true) @RequestHeader("Authorization") String token,
            RecordQueryRequest request) {
        Long userId = getUserId(token);
        Map<String, Object> result = recordService.queryRecords(userId, request);
        return Result.success(result);
    }

    /**
     * 获取分类列表
     * @return 分类列表
     */
    @ApiOperation("获取分类列表")
    @GetMapping("/categories")
    public Result<Map<String, Object>> getCategories() {
        Map<String, Object> data = new HashMap<>();
        data.put("incomeCategories", RecordService.INCOME_CATEGORIES);
        data.put("expenseCategories", RecordService.EXPENSE_CATEGORIES);
        return Result.success(data);
    }
}
