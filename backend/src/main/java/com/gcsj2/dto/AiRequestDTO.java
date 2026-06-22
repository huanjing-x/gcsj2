package com.gcsj2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * AI 请求 DTO
 */
@Data
public class AiRequestDTO {

    @NotBlank(message = "代码内容不能为空")
    private String codeContent;

    @NotBlank(message = "编程语言不能为空")
    private String language;

    /**
     * 操作类型：
     *   comment  - 添加注释
     *   debug    - BUG检测与修复
     *   refactor - 精简重构
     */
    @NotBlank(message = "操作类型不能为空")
    private String actionType;
}
