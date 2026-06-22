package com.gcsj2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 标签保存 DTO
 */
@Data
public class TagSaveDTO {

    /** 编辑时传入ID */
    private Long id;

    @NotBlank(message = "标签名称不能为空")
    private String tagName;
}
