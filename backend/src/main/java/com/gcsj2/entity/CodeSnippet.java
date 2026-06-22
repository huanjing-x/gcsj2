package com.gcsj2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 代码片段实体
 */
@Data
@TableName("code_snippet")
public class CodeSnippet {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属用户id */
    private Long userId;

    /** 分类id */
    private Long categoryId;

    /** 代码标题 */
    private String title;

    /** 编程语言 */
    private String language;

    /** 完整代码内容 */
    private String codeContent;

    /** 备注说明 */
    private String remark;

    /** 是否收藏：0-未收藏 1-已收藏 */
    private Integer isCollect;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
