package com.gcsj2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 代码片段保存 DTO（新增/编辑共用）
 */
@Data
public class SnippetSaveDTO {

    /** 编辑时传入ID */
    private Long id;

    @NotBlank(message = "代码标题不能为空")
    private String title;

    @NotBlank(message = "编程语言不能为空")
    private String language;

    /** 分类ID（可为空） */
    private Long categoryId;

    @NotBlank(message = "代码内容不能为空")
    private String codeContent;

    /** 备注说明 */
    private String remark;

    /** 标签ID列表 */
    private List<Long> tagIds;
}
