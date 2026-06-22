package com.gcsj2.dto;

import lombok.Data;

/**
 * 代码片段查询 DTO
 */
@Data
public class SnippetQueryDTO {

    /** 标题模糊搜索 */
    private String title;

    /** 编程语言筛选 */
    private String language;

    /** 分类ID筛选 */
    private Long categoryId;

    /** 标签ID筛选（通过关联表查询） */
    private Long tagId;

    /** 是否仅看收藏 */
    private Boolean isCollect;

    /** 当前页码 */
    private Integer current = 1;

    /** 每页条数 */
    private Integer size = 10;
}
