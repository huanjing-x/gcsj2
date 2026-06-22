package com.gcsj2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gcsj2.dto.CategorySaveDTO;
import com.gcsj2.entity.CodeCategory;

import java.util.List;

/**
 * 代码分类服务接口
 */
public interface CodeCategoryService {

    /**
     * 新增分类
     */
    void add(CategorySaveDTO dto);

    /**
     * 编辑分类
     */
    void update(CategorySaveDTO dto);

    /**
     * 删除分类（分类下无代码才可删除）
     */
    void delete(Long id);

    /**
     * 分页查询当前用户的分类
     */
    IPage<CodeCategory> page(int current, int size);

    /**
     * 查询当前用户所有分类（用于下拉框）
     */
    List<CodeCategory> listAll();
}
