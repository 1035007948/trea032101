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
 * 处理用户相关的HTTP请求
 */
@RestController
@RequestMapping("/api/user")
@Api(value = "用户管理", tags = "用户管理接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param request 注册请求对象
     * @return 注册结果
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "新用户注册接口")
    public Result<Void> register(
            @ApiParam(value = "注册信息", required = true)
            @Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success();
    }

    /**
     * 用户登录
     *
     * @param request 登录请求对象
     * @return 登录结果，包含token
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录接口，返回token")
    public Result<Map<String, String>> login(
            @ApiParam(value = "登录信息", required = true)
            @Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return Result.success(data);
    }

    /**
     * 修改密码
     *
     * @param token   用户token
     * @param request 修改密码请求对象
     * @return 修改结果
     */
    @PostMapping("/changePassword")
    @ApiOperation(value = "修改密码", notes = "修改当前登录用户的密码")
    public Result<Void> changePassword(
            @ApiParam(value = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @ApiParam(value = "密码修改信息", required = true)
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
     *
     * @param token 用户token
     * @return 用户信息
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户的基本信息")
    public Result<Map<String, Object>> getUserInfo(
            @ApiParam(value = "用户token", required = true)
            @RequestHeader("Authorization") String token) {
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
