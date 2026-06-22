package com.gcsj2.controller;

import com.gcsj2.common.Result;
import com.gcsj2.dto.LoginDTO;
import com.gcsj2.dto.PasswordUpdateDTO;
import com.gcsj2.dto.RegisterDTO;
import com.gcsj2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.ok("注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        Map<String, Object> result = userService.login(dto);
        return Result.ok("登录成功", result);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<?> info() {
        return Result.ok(userService.getCurrentUser());
    }

    /**
     * 修改昵称
     */
    @PutMapping("/nickname")
    public Result<Void> updateNickName(@RequestBody Map<String, String> body) {
        userService.updateNickName(body.get("nickName"));
        return Result.ok("昵称修改成功");
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody PasswordUpdateDTO dto) {
        userService.updatePassword(dto);
        return Result.ok("密码修改成功");
    }
}
