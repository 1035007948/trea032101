package com.accounting.controller;

import com.accounting.common.BusinessException;
import com.accounting.common.Result;
import com.accounting.service.RecordService;
import com.accounting.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Map;

@Api(tags = "统计分析接口")
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private UserService userService;

    private static final ZoneId SOURCE_ZONE = ZoneId.of("UTC");
    private static final ZoneId TARGET_ZONE = ZoneId.of("Asia/Shanghai");

    private Long getUserId(String token) {
        Long userId = userService.getUserIdByToken(token);
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }

    @ApiOperation("获取周统计数据")
    @GetMapping("/weekly")
    public Result<Map<String, Object>> getWeeklyStats(
            @ApiParam("认证令牌") @RequestHeader("Authorization") String token) {
        Long userId = getUserId(token);
        Map<String, Object> stats = recordService.getWeeklyStats(userId);
        return Result.success(stats);
    }

    @ApiOperation("获取月统计数据")
    @GetMapping("/monthly")
    public Result<Map<String, Object>> getMonthlyStats(
            @ApiParam("认证令牌") @RequestHeader("Authorization") String token) {
        Long userId = getUserId(token);
        Map<String, Object> stats = recordService.getMonthlyStats(userId);
        return Result.success(stats);
    }

    @ApiOperation("获取分类统计数据")
    @GetMapping("/category")
    public Result<Map<String, Object>> getCategoryStats(
            @ApiParam("认证令牌") @RequestHeader("Authorization") String token,
            @ApiParam("记录类型") @RequestParam(required = false) String type) {
        Long userId = getUserId(token);
        Map<String, Object> stats = recordService.getCategoryStats(userId, type);
        return Result.success(stats);
    }
}
