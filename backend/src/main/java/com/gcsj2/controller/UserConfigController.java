package com.gcsj2.controller;

import com.gcsj2.common.Result;
import com.gcsj2.service.UserConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户配置控制器
 */
@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class UserConfigController {

    private final UserConfigService configService;

    /**
     * 获取当前用户所有配置
     */
    @GetMapping
    public Result<Map<String, String>> getAllConfig() {
        return Result.ok(configService.getAllConfig());
    }

    /**
     * 保存配置
     */
    @PutMapping
    public Result<Void> saveConfig(@RequestBody Map<String, String> configs) {
        configService.saveConfigs(configs);
        return Result.ok("保存成功");
    }
}
