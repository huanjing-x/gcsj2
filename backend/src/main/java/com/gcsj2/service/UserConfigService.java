package com.gcsj2.service;

import java.util.Map;

/**
 * 用户配置服务接口
 */
public interface UserConfigService {

    /**
     * 获取当前用户所有配置（键值对）
     */
    Map<String, String> getAllConfig();

    /**
     * 获取单个配置值
     */
    String getConfig(String key);

    /**
     * 保存/更新配置
     */
    void saveConfig(String key, String value);

    /**
     * 批量保存配置
     */
    void saveConfigs(Map<String, String> configs);
}
