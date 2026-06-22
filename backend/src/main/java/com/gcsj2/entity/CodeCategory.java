package com.gcsj2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 代码分类实体
 */
@Data
@TableName("code_category")
public class CodeCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属用户id */
    private Long userId;

    /** 分类名称 */
    private String categoryName;

    /** 排序号 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;
}
