package com.gcsj2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 代码-标签关联实体
 */
@Data
@TableName("snippet_tag_rel")
public class SnippetTagRel {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 代码片段id */
    private Long snippetId;

    /** 标签id */
    private Long tagId;
}
