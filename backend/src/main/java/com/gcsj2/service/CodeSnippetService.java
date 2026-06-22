package com.gcsj2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gcsj2.dto.SnippetQueryDTO;
import com.gcsj2.dto.SnippetSaveDTO;
import com.gcsj2.entity.CodeSnippet;

import java.util.Map;

/**
 * 代码片段服务接口
 */
public interface CodeSnippetService {

    /**
     * 新增代码片段
     */
    void add(SnippetSaveDTO dto);

    /**
     * 编辑代码片段
     */
    void update(SnippetSaveDTO dto);

    /**
     * 删除代码片段
     */
    void delete(Long id);

    /**
     * 分页查询（含筛选条件）
     */
    IPage<Map<String, Object>> page(SnippetQueryDTO dto);

    /**
     * 获取代码详情
     */
    Map<String, Object> getDetail(Long id);

    /**
     * 切换收藏状态
     */
    void toggleCollect(Long id);

    /**
     * 复制代码内容
     */
    String getCodeContent(Long id);
}
