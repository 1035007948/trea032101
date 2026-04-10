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
 * 用户服务层
 * 处理用户相关的业务逻辑
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Token存储，key为token，value为用户ID
     */
    private final Map<String, Long> tokenStore = new HashMap<>();

    /**
     * 用户注册
     *
     * @param request 注册请求对象
     * @throws BusinessException 当账号已存在时抛出
     */
    public void register(RegisterRequest request) {
        // 检查账号是否已存在
        if (userRepository.findByAccount(request.getAccount()).isPresent()) {
            throw new BusinessException("账号已存在");
        }
        // 创建新用户
        User user = new User();
        user.setAccount(request.getAccount());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getAccount());
        userRepository.save(user);
    }

    /**
     * 用户登录
     *
     * @param request 登录请求对象
     * @return 登录成功后生成的token
     * @throws BusinessException 当账号或密码错误时抛出
     */
    public String login(LoginRequest request) {
        // 根据账号查询用户
        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(() -> new BusinessException("账号或密码错误"));
        // 验证密码
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        // 生成token并存储
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenStore.put(token, user.getId());
        return token;
    }

    /**
     * 修改密码
     *
     * @param userId  用户ID
     * @param request 修改密码请求对象
     * @throws BusinessException 当用户不存在或原密码错误时抛出
     */
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // 查询用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        // 验证原密码
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new BusinessException("原密码错误");
        }
        // 更新密码
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    /**
     * 根据token获取用户ID
     *
     * @param token 用户token
     * @return 用户ID，如果token无效返回null
     */
    public Long getUserIdByToken(String token) {
        if (token == null || token.startsWith("Bearer ")) {
            return null;
        }
        return tokenStore.get(token);
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户对象，如果不存在返回null
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
