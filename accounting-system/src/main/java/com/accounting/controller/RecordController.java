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

@Api(tags = "收支记录管理接口")
@RestController
@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private UserService userService;

    private Long getUserId(String token) {
        Long userId = userService.getUserIdByToken(token);
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }

    @ApiOperation("添加收支记录")
    @PostMapping("/add")
    public Result<Record> addRecord(
            @ApiParam("认证令牌") @RequestHeader("Authorization") String token,
            @ApiParam("记录信息") @Valid @RequestBody AddRecordRequest request) {
        Long userId = getUserId(token);
        Record record = recordService.addRecord(userId, request);
        return Result.success(record);
    }

    @ApiOperation("更新收支记录")
    @PostMapping("/update")
    public Result<Record> updateRecord(
            @ApiParam("认证令牌") @RequestHeader("Authorization") String token,
            @ApiParam("更新信息") @Valid @RequestBody UpdateRecordRequest request) {
        Long userId = getUserId(token);
        Record record = recordService.updateRecord(userId, request);
        return Result.success(record);
    }

    @ApiOperation("删除收支记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRecord(
            @ApiParam("认证令牌") @RequestHeader("Authorization") String token,
            @ApiParam("记录ID") @PathVariable Long id) {
        Long userId = getUserId(token);
        recordService.deleteRecord(userId, id);
        return Result.success();
    }

    @ApiOperation("查询收支记录列表")
    @GetMapping("/list")
    public Result<Map<String, Object>> queryRecords(
            @ApiParam("认证令牌") @RequestHeader("Authorization") String token,
            @ApiParam("查询条件") RecordQueryRequest request) {
        Long userId = getUserId(token);
        Map<String, Object> result = recordService.queryRecords(userId, request);
        return Result.success(result);
    }

    @ApiOperation("获取分类列表")
    @GetMapping("/categories")
    public Result<Map<String, Object>> getCategories() {
        Map<String, Object> data = new HashMap<>();
        data.put("incomeCategories", RecordService.INCOME_CATEGORIES);
        data.put("expenseCategories", RecordService.EXPENSE_CATEGORIES);
        return Result.success(data);
    }
}
