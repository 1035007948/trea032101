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

/**
 * 统计控制器
 * 处理记账统计相关请求
 */
@Api(tags = "统计分析")
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private UserService userService;

    /**
     * 源时区（UTC）
     */
    private static final ZoneId SOURCE_ZONE = ZoneId.of("UTC");
    
    /**
     * 目标时区（上海）
     */
    private static final ZoneId TARGET_ZONE = ZoneId.of("Asia/Shanghai");

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
     * 获取本周统计数据
     * @param token 用户token
     * @return 本周统计结果
     */
    @ApiOperation("获取本周统计")
    @GetMapping("/weekly")
    public Result<Map<String, Object>> getWeeklyStats(
            @ApiParam(value = "用户token", required = true) @RequestHeader("Authorization") String token) {
        Long userId = getUserId(token);
        Map<String, Object> stats = recordService.getWeeklyStats(userId);
        return Result.success(stats);
    }

    /**
     * 获取本月统计数据
     * @param token 用户token
     * @return 本月统计结果
     */
    @ApiOperation("获取本月统计")
    @GetMapping("/monthly")
    public Result<Map<String, Object>> getMonthlyStats(
            @ApiParam(value = "用户token", required = true) @RequestHeader("Authorization") String token) {
        Long userId = getUserId(token);
        Map<String, Object> stats = recordService.getMonthlyStats(userId);
        return Result.success(stats);
    }

    /**
     * 获取分类统计数据
     * @param token 用户token
     * @param type 类型（收入/支出），为空则统计全部
     * @return 分类统计结果
     */
    @ApiOperation("获取分类统计")
    @GetMapping("/category")
    public Result<Map<String, Object>> getCategoryStats(
            @ApiParam(value = "用户token", required = true) @RequestHeader("Authorization") String token,
            @ApiParam(value = "类型（收入/支出）") @RequestParam(required = false) String type) {
        Long userId = getUserId(token);
        Map<String, Object> stats = recordService.getCategoryStats(userId, type);
        return Result.success(stats);
    }
}
