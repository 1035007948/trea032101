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

import java.util.Map;

/**
 * 统计控制器
 * 处理收支统计相关的HTTP请求
 */
@RestController
@RequestMapping("/api/stats")
@Api(value = "统计分析", tags = "统计分析接口")
public class StatsController {

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
     * 获取本周统计
     *
     * @param token 用户token
     * @return 本周收支统计信息
     */
    @GetMapping("/weekly")
    @ApiOperation(value = "本周统计", notes = "获取本周的收支统计信息（总收入、总支出、结余）")
    public Result<Map<String, Object>> getWeeklyStats(
            @ApiParam(value = "用户token", required = true)
            @RequestHeader("Authorization") String token) {
        Long userId = getUserId(token);
        Map<String, Object> stats = recordService.getWeeklyStats(userId);
        return Result.success(stats);
    }

    /**
     * 获取本月统计
     *
     * @param token 用户token
     * @return 本月收支统计信息
     */
    @GetMapping("/monthly")
    @ApiOperation(value = "本月统计", notes = "获取本月的收支统计信息（总收入、总支出、结余）")
    public Result<Map<String, Object>> getMonthlyStats(
            @ApiParam(value = "用户token", required = true)
            @RequestHeader("Authorization") String token) {
        Long userId = getUserId(token);
        Map<String, Object> stats = recordService.getMonthlyStats(userId);
        return Result.success(stats);
    }

    /**
     * 获取分类统计
     *
     * @param token 用户token
     * @param type  类型筛选（可选，收入/支出）
     * @return 分类统计信息
     */
    @GetMapping("/category")
    @ApiOperation(value = "分类统计", notes = "按分类统计收支情况，可筛选类型")
    public Result<Map<String, Object>> getCategoryStats(
            @ApiParam(value = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "类型筛选（收入/支出）", example = "支出")
            @RequestParam(required = false) String type) {
        Long userId = getUserId(token);
        Map<String, Object> stats = recordService.getCategoryStats(userId, type);
        return Result.success(stats);
    }
}
