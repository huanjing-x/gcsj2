package com.gcsj2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户配置实体
 */
@Data
@TableName("user_config")
public class UserConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属用户id */
    private Long userId;

    /** 配置键 */
    private String configKey;

    /** 配置值 */
    private String configValue;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
