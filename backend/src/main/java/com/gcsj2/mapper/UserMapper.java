package com.gcsj2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcsj2.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
