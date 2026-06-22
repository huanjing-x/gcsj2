package com.gcsj2.service;

import com.gcsj2.dto.LoginDTO;
import com.gcsj2.dto.PasswordUpdateDTO;
import com.gcsj2.dto.RegisterDTO;
import com.gcsj2.entity.User;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    void register(RegisterDTO dto);

    /**
     * 用户登录，返回令牌和用户信息
     */
    Map<String, Object> login(LoginDTO dto);

    /**
     * 获取当前用户信息
     */
    User getCurrentUser();

    /**
     * 修改昵称
     */
    void updateNickName(String nickName);

    /**
     * 修改密码
     */
    void updatePassword(PasswordUpdateDTO dto);
}
