package com.gcsj2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gcsj2.dto.TagSaveDTO;
import com.gcsj2.entity.CodeTag;

import java.util.List;

/**
 * 代码标签服务接口
 */
public interface CodeTagService {

    /**
     * 新增标签
     */
    void add(TagSaveDTO dto);

    /**
     * 编辑标签
     */
    void update(TagSaveDTO dto);

    /**
     * 删除标签
     */
    void delete(Long id);

    /**
     * 分页查询当前用户的标签
     */
    IPage<CodeTag> page(int current, int size);

    /**
     * 查询当前用户所有标签（用于下拉框/筛选）
     */
    List<CodeTag> listAll();
}
