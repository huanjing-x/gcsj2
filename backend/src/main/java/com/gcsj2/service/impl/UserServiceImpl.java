package com.gcsj2.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gcsj2.dto.LoginDTO;
import com.gcsj2.dto.PasswordUpdateDTO;
import com.gcsj2.dto.RegisterDTO;
import com.gcsj2.entity.User;
import com.gcsj2.exception.BusinessException;
import com.gcsj2.mapper.UserMapper;
import com.gcsj2.security.JwtUtil;
import com.gcsj2.security.UserContext;
import com.gcsj2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void register(RegisterDTO dto) {
        // 校验两次密码是否一致
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }

        // 校验账号是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该账号已被注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        // BCrypt 加密密码
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setNickName(dto.getNickName() != null ? dto.getNickName() : dto.getUsername());
        userMapper.insert(user);
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException("账号不存在");
        }

        // 校验密码
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 生成 JWT 令牌
        boolean rememberMe = dto.getRememberMe() != null && dto.getRememberMe();
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), rememberMe);

        // 返回令牌和用户信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickName", user.getNickName());
        return result;
    }

    @Override
    public User getCurrentUser() {
        Long userId = UserContext.getCurrentUserId();
        User user = userMapper.selectById(userId);
        // 脱敏：不返回密码
        user.setPassword(null);
        return user;
    }

    @Override
    @Transactional
    public void updateNickName(String nickName) {
        Long userId = UserContext.getCurrentUserId();
        User user = new User();
        user.setId(userId);
        user.setNickName(nickName);
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void updatePassword(PasswordUpdateDTO dto) {
        // 校验新密码一致性
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }

        Long userId = UserContext.getCurrentUserId();
        User user = userMapper.selectById(userId);

        // 校验原密码
        if (!BCrypt.checkpw(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 更新密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(BCrypt.hashpw(dto.getNewPassword()));
        userMapper.updateById(updateUser);
    }
}
