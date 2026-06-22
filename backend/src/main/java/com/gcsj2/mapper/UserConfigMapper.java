package com.gcsj2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcsj2.entity.UserConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户配置 Mapper
 */
@Mapper
public interface UserConfigMapper extends BaseMapper<UserConfig> {
}
