package com.accounting.service;

import com.accounting.common.BusinessException;
import com.accounting.dto.ChangePasswordRequest;
import com.accounting.dto.LoginRequest;
import com.accounting.dto.RegisterRequest;
import com.accounting.entity.User;
import com.accounting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户服务类
 * 处理用户注册、登录、密码修改等业务逻辑
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final Map<String, Long> tokenStore = new HashMap<>();

    /**
     * 用户注册
     * @param request 注册请求参数
     * @throws BusinessException 账号已存在时抛出异常
     */
    public void register(RegisterRequest request) {
        if (userRepository.findByAccount(request.getAccount()).isPresent()) {
            throw new BusinessException("账号已存在");
        }
        User user = new User();
        user.setAccount(request.getAccount());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getAccount());
        userRepository.save(user);
    }

    /**
     * 用户登录
     * @param request 登录请求参数
     * @return 登录成功返回token令牌
     * @throws BusinessException 账号或密码错误时抛出异常
     */
    public String login(LoginRequest request) {
        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(() -> new BusinessException("账号或密码错误"));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenStore.put(token, user.getId());
        return token;
    }

    /**
     * 修改密码
     * @param userId 用户ID
     * @param request 修改密码请求参数
     * @throws BusinessException 用户不存在或原密码错误时抛出异常
     */
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new BusinessException("原密码错误");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    /**
     * 根据token获取用户ID
     * @param token 认证令牌
     * @return 用户ID，token无效时返回null
     */
    public Long getUserIdByToken(String token) {
        if (token == null || token.startsWith("Bearer ")) {
            return null;
        }
        return tokenStore.get(token);
    }

    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户实体
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
