package com.gcsj2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gcsj2.entity.UserConfig;
import com.gcsj2.mapper.UserConfigMapper;
import com.gcsj2.security.UserContext;
import com.gcsj2.service.UserConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户配置服务实现
 */
@Service
@RequiredArgsConstructor
public class UserConfigServiceImpl implements UserConfigService {

    private final UserConfigMapper configMapper;

    @Override
    public Map<String, String> getAllConfig() {
        Long userId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<UserConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserConfig::getUserId, userId);
        List<UserConfig> list = configMapper.selectList(wrapper);

        Map<String, String> result = new HashMap<>();
        for (UserConfig config : list) {
            result.put(config.getConfigKey(), config.getConfigValue());
        }
        return result;
    }

    @Override
    public String getConfig(String key) {
        Long userId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<UserConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserConfig::getUserId, userId)
               .eq(UserConfig::getConfigKey, key);
        UserConfig config = configMapper.selectOne(wrapper);
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    @Transactional
    public void saveConfig(String key, String value) {
        Long userId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<UserConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserConfig::getUserId, userId)
               .eq(UserConfig::getConfigKey, key);
        UserConfig config = configMapper.selectOne(wrapper);

        if (config != null) {
            config.setConfigValue(value);
            configMapper.updateById(config);
        } else {
            config = new UserConfig();
            config.setUserId(userId);
            config.setConfigKey(key);
            config.setConfigValue(value);
            configMapper.insert(config);
        }
    }

    @Override
    @Transactional
    public void saveConfigs(Map<String, String> configs) {
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            saveConfig(entry.getKey(), entry.getValue());
        }
    }
}
