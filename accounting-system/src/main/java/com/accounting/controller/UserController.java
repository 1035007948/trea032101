package com.accounting.controller;

import com.accounting.common.BusinessException;
import com.accounting.common.Result;
import com.accounting.dto.ChangePasswordRequest;
import com.accounting.dto.LoginRequest;
import com.accounting.dto.RegisterRequest;
import com.accounting.entity.User;
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
 * 用户控制器
 * 处理用户注册、登录、信息查询等请求
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param request 注册请求参数
     * @return 操作结果
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success();
    }

    /**
     * 用户登录
     * @param request 登录请求参数
     * @return 登录成功返回token
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return Result.success(data);
    }

    /**
     * 修改密码
     * @param token 用户token
     * @param request 修改密码请求参数
     * @return 操作结果
     */
    @ApiOperation("修改密码")
    @PostMapping("/changePassword")
    public Result<Void> changePassword(
            @ApiParam(value = "用户token", required = true) @RequestHeader("Authorization") String token,
            @Valid @RequestBody ChangePasswordRequest request) {
        Long userId = userService.getUserIdByToken(token);
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        userService.changePassword(userId, request);
        return Result.success();
    }

    /**
     * 获取用户信息
     * @param token 用户token
     * @return 用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(
            @ApiParam(value = "用户token", required = true) @RequestHeader("Authorization") String token) {
        Long userId = userService.getUserIdByToken(token);
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        User user = userService.getUserById(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("account", user.getAccount());
        data.put("nickname", user.getNickname());
        return Result.success(data);
    }
}
