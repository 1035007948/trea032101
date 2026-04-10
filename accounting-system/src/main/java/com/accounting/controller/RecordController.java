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
import java.util.Map;

/**
 * 收支记录控制器
 * 处理收支记录相关的HTTP请求
 */
@RestController
@RequestMapping("/api/record")
@Api(value = "收支记录管理", tags = "收支记录管理接口")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户ID
     *
     * @param token 用户token
     * @return 用户ID
     * @throws BusinessException 当用户未登录时抛出
     */
    private Long getUserId(String token) {
        Long userId = userService.getUserIdByToken(token);
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }

    /**
     * 添加收支记录
     *
     * @param token   用户token
     * @param request 添加记录请求对象
     * @return 添加的记录
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加记录", notes = "添加一条收支记录")
    public Result<Record> addRecord(
            @ApiParam(value = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "记录信息", required = true)
            @Valid @RequestBody AddRecordRequest request) {
        Long userId = getUserId(token);
        Record record = recordService.addRecord(userId, request);
        return Result.success(record);
    }

    /**
     * 更新收支记录
     *
     * @param token   用户token
     * @param request 更新记录请求对象
     * @return 更新后的记录
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新记录", notes = "更新一条收支记录")
    public Result<Record> updateRecord(
            @ApiParam(value = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "记录信息", required = true)
            @Valid @RequestBody UpdateRecordRequest request) {
        Long userId = getUserId(token);
        Record record = recordService.updateRecord(userId, request);
        return Result.success(record);
    }

    /**
     * 删除收支记录
     *
     * @param token    用户token
     * @param id       记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除记录", notes = "删除一条收支记录")
    public Result<Void> deleteRecord(
            @ApiParam(value = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "记录ID", required = true)
            @PathVariable Long id) {
        Long userId = getUserId(token);
        recordService.deleteRecord(userId, id);
        return Result.success();
    }

    /**
     * 查询收支记录列表
     *
     * @param token   用户token
     * @param request 查询请求对象
     * @return 记录列表和分页信息
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询记录列表", notes = "分页查询收支记录列表，支持按时间、类型、分类筛选")
    public Result<Map<String, Object>> queryRecords(
            @ApiParam(value = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "查询条件")
            RecordQueryRequest request) {
        Long userId = getUserId(token);
        Map<String, Object> result = recordService.queryRecords(userId, request);
        return Result.success(result);
    }

    /**
     * 获取分类列表
     *
     * @return 收入和支出分类列表
     */
    @GetMapping("/categories")
    @ApiOperation(value = "获取分类列表", notes = "获取系统预定义的收入和支出分类列表")
    public Result<Map<String, Object>> getCategories() {
        Map<String, Object> data = new HashMap<>();
        data.put("incomeCategories", RecordService.INCOME_CATEGORIES);
        data.put("expenseCategories", RecordService.EXPENSE_CATEGORIES);
        return Result.success(data);
    }
}
