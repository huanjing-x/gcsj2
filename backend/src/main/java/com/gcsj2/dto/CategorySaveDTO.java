package com.gcsj2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 分类保存 DTO
 */
@Data
public class CategorySaveDTO {

    /** 编辑时传入ID */
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String categoryName;

    /** 排序序号 */
    private Integer sort;
}
