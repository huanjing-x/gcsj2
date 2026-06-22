package com.gcsj2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gcsj2.dto.SnippetQueryDTO;
import com.gcsj2.dto.SnippetSaveDTO;
import com.gcsj2.entity.*;
import com.gcsj2.exception.BusinessException;
import com.gcsj2.mapper.*;
import com.gcsj2.security.UserContext;
import com.gcsj2.service.CodeSnippetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码片段服务实现
 */
@Service
@RequiredArgsConstructor
public class CodeSnippetServiceImpl implements CodeSnippetService {

    private final CodeSnippetMapper snippetMapper;
    private final SnippetTagRelMapper relMapper;
    private final CodeTagMapper tagMapper;
    private final CodeCategoryMapper categoryMapper;

    @Override
    @Transactional
    public void add(SnippetSaveDTO dto) {
        Long userId = UserContext.getCurrentUserId();

        CodeSnippet snippet = new CodeSnippet();
        snippet.setUserId(userId);
        snippet.setCategoryId(dto.getCategoryId());
        snippet.setTitle(dto.getTitle());
        snippet.setLanguage(dto.getLanguage());
        snippet.setCodeContent(dto.getCodeContent());
        snippet.setRemark(dto.getRemark());
        snippet.setIsCollect(0);
        snippetMapper.insert(snippet);

        // 保存标签关联
        saveTagRelations(snippet.getId(), dto.getTagIds());
    }

    @Override
    @Transactional
    public void update(SnippetSaveDTO dto) {
        Long userId = UserContext.getCurrentUserId();

        CodeSnippet snippet = snippetMapper.selectById(dto.getId());
        if (snippet == null || !snippet.getUserId().equals(userId)) {
            throw new BusinessException("代码片段不存在");
        }

        snippet.setCategoryId(dto.getCategoryId());
        snippet.setTitle(dto.getTitle());
        snippet.setLanguage(dto.getLanguage());
        snippet.setCodeContent(dto.getCodeContent());
        snippet.setRemark(dto.getRemark());
        snippetMapper.updateById(snippet);

        // 先删除原有标签关联，再重新插入
        LambdaQueryWrapper<SnippetTagRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SnippetTagRel::getSnippetId, dto.getId());
        relMapper.delete(wrapper);

        saveTagRelations(dto.getId(), dto.getTagIds());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long userId = UserContext.getCurrentUserId();
        CodeSnippet snippet = snippetMapper.selectById(id);
        if (snippet == null || !snippet.getUserId().equals(userId)) {
            throw new BusinessException("代码片段不存在");
        }

        // 删除标签关联
        LambdaQueryWrapper<SnippetTagRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SnippetTagRel::getSnippetId, id);
        relMapper.delete(wrapper);

        // 删除代码片段
        snippetMapper.deleteById(id);
    }

    @Override
    public IPage<Map<String, Object>> page(SnippetQueryDTO dto) {
        Long userId = UserContext.getCurrentUserId();

        // 如果按标签筛选，先查出关联的 snippet_id 列表
        Set<Long> tagSnippetIds = null;
        if (dto.getTagId() != null) {
            LambdaQueryWrapper<SnippetTagRel> relWrapper = new LambdaQueryWrapper<>();
            relWrapper.eq(SnippetTagRel::getTagId, dto.getTagId());
            tagSnippetIds = relMapper.selectList(relWrapper).stream()
                    .map(SnippetTagRel::getSnippetId)
                    .collect(Collectors.toSet());
            if (tagSnippetIds.isEmpty()) {
                // 没有匹配的代码片段，直接返回空页
                Page<Map<String, Object>> emptyPage = new Page<>(dto.getCurrent(), dto.getSize());
                emptyPage.setTotal(0);
                emptyPage.setRecords(Collections.emptyList());
                return emptyPage;
            }
        }

        // 构建查询条件
        LambdaQueryWrapper<CodeSnippet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CodeSnippet::getUserId, userId);

        if (StringUtils.hasText(dto.getTitle())) {
            wrapper.like(CodeSnippet::getTitle, dto.getTitle());
        }
        if (StringUtils.hasText(dto.getLanguage())) {
            wrapper.eq(CodeSnippet::getLanguage, dto.getLanguage());
        }
        if (dto.getCategoryId() != null) {
            wrapper.eq(CodeSnippet::getCategoryId, dto.getCategoryId());
        }
        if (dto.getIsCollect() != null && dto.getIsCollect()) {
            wrapper.eq(CodeSnippet::getIsCollect, 1);
        }
        if (tagSnippetIds != null) {
            wrapper.in(CodeSnippet::getId, tagSnippetIds);
        }

        wrapper.orderByDesc(CodeSnippet::getUpdateTime);

        // 分页查询
        Page<CodeSnippet> page = new Page<>(dto.getCurrent(), dto.getSize());
        IPage<CodeSnippet> snippetPage = snippetMapper.selectPage(page, wrapper);

        // 转换为 Map，附带分类名称和标签列表
        List<Map<String, Object>> records = snippetPage.getRecords().stream()
                .map(snippet -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", snippet.getId());
                    map.put("userId", snippet.getUserId());
                    map.put("categoryId", snippet.getCategoryId());
                    map.put("title", snippet.getTitle());
                    map.put("language", snippet.getLanguage());
                    map.put("codeContent", snippet.getCodeContent());
                    map.put("remark", snippet.getRemark());
                    map.put("isCollect", snippet.getIsCollect());
                    map.put("createTime", snippet.getCreateTime());
                    map.put("updateTime", snippet.getUpdateTime());

                    // 分类名称
                    if (snippet.getCategoryId() != null) {
                        CodeCategory category = categoryMapper.selectById(snippet.getCategoryId());
                        map.put("categoryName", category != null ? category.getCategoryName() : "");
                    } else {
                        map.put("categoryName", "");
                    }

                    // 标签列表
                    List<CodeTag> tags = getTagsBySnippetId(snippet.getId());
                    map.put("tags", tags);

                    return map;
                })
                .collect(Collectors.toList());

        // 构建返回分页
        Page<Map<String, Object>> resultPage = new Page<>(dto.getCurrent(), dto.getSize());
        resultPage.setTotal(snippetPage.getTotal());
        resultPage.setRecords(records);
        return resultPage;
    }

    @Override
    public Map<String, Object> getDetail(Long id) {
        Long userId = UserContext.getCurrentUserId();
        CodeSnippet snippet = snippetMapper.selectById(id);
        if (snippet == null || !snippet.getUserId().equals(userId)) {
            throw new BusinessException("代码片段不存在");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", snippet.getId());
        map.put("title", snippet.getTitle());
        map.put("language", snippet.getLanguage());
        map.put("codeContent", snippet.getCodeContent());
        map.put("remark", snippet.getRemark());
        map.put("isCollect", snippet.getIsCollect());
        map.put("categoryId", snippet.getCategoryId());
        map.put("createTime", snippet.getCreateTime());

        if (snippet.getCategoryId() != null) {
            CodeCategory category = categoryMapper.selectById(snippet.getCategoryId());
            map.put("categoryName", category != null ? category.getCategoryName() : "");
        }

        map.put("tags", getTagsBySnippetId(snippet.getId()));
        return map;
    }

    @Override
    @Transactional
    public void toggleCollect(Long id) {
        Long userId = UserContext.getCurrentUserId();
        CodeSnippet snippet = snippetMapper.selectById(id);
        if (snippet == null || !snippet.getUserId().equals(userId)) {
            throw new BusinessException("代码片段不存在");
        }

        snippet.setIsCollect(snippet.getIsCollect() == 1 ? 0 : 1);
        snippetMapper.updateById(snippet);
    }

    @Override
    public String getCodeContent(Long id) {
        Long userId = UserContext.getCurrentUserId();
        CodeSnippet snippet = snippetMapper.selectById(id);
        if (snippet == null || !snippet.getUserId().equals(userId)) {
            throw new BusinessException("代码片段不存在");
        }
        return snippet.getCodeContent();
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 保存标签关联
     */
    private void saveTagRelations(Long snippetId, List<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                SnippetTagRel rel = new SnippetTagRel();
                rel.setSnippetId(snippetId);
                rel.setTagId(tagId);
                relMapper.insert(rel);
            }
        }
    }

    /**
     * 根据代码片段ID获取标签列表
     */
    private List<CodeTag> getTagsBySnippetId(Long snippetId) {
        LambdaQueryWrapper<SnippetTagRel> relWrapper = new LambdaQueryWrapper<>();
        relWrapper.eq(SnippetTagRel::getSnippetId, snippetId);
        List<SnippetTagRel> rels = relMapper.selectList(relWrapper);

        if (rels.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> tagIds = rels.stream()
                .map(SnippetTagRel::getTagId)
                .collect(Collectors.toList());
        return tagMapper.selectBatchIds(tagIds);
    }
}
