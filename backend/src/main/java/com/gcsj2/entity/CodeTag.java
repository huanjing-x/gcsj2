package com.gcsj2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 代码标签实体
 */
@Data
@TableName("code_tag")
public class CodeTag {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属用户id */
    private Long userId;

    /** 标签名 */
    private String tagName;

    /** 创建时间 */
    private LocalDateTime createTime;
}
