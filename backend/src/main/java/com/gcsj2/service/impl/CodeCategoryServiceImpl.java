package com.gcsj2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gcsj2.dto.CategorySaveDTO;
import com.gcsj2.entity.CodeCategory;
import com.gcsj2.entity.CodeSnippet;
import com.gcsj2.exception.BusinessException;
import com.gcsj2.mapper.CodeCategoryMapper;
import com.gcsj2.mapper.CodeSnippetMapper;
import com.gcsj2.security.UserContext;
import com.gcsj2.service.CodeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 代码分类服务实现
 */
@Service
@RequiredArgsConstructor
public class CodeCategoryServiceImpl implements CodeCategoryService {

    private final CodeCategoryMapper categoryMapper;
    private final CodeSnippetMapper snippetMapper;

    @Override
    @Transactional
    public void add(CategorySaveDTO dto) {
        Long userId = UserContext.getCurrentUserId();
        CodeCategory category = new CodeCategory();
        category.setUserId(userId);
        category.setCategoryName(dto.getCategoryName());
        category.setSort(dto.getSort() != null ? dto.getSort() : 0);
        categoryMapper.insert(category);
    }

    @Override
    @Transactional
    public void update(CategorySaveDTO dto) {
        Long userId = UserContext.getCurrentUserId();
        // 查询原分类，确保数据归属
        CodeCategory category = categoryMapper.selectById(dto.getId());
        if (category == null || !category.getUserId().equals(userId)) {
            throw new BusinessException("分类不存在");
        }

        category.setCategoryName(dto.getCategoryName());
        if (dto.getSort() != null) {
            category.setSort(dto.getSort());
        }
        categoryMapper.updateById(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long userId = UserContext.getCurrentUserId();
        CodeCategory category = categoryMapper.selectById(id);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new BusinessException("分类不存在");
        }

        // 检查该分类下是否有代码
        LambdaQueryWrapper<CodeSnippet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CodeSnippet::getCategoryId, id)
               .eq(CodeSnippet::getUserId, userId);
        if (snippetMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该分类下存在代码片段，无法删除");
        }

        categoryMapper.deleteById(id);
    }

    @Override
    public IPage<CodeCategory> page(int current, int size) {
        Long userId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<CodeCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CodeCategory::getUserId, userId)
               .orderByAsc(CodeCategory::getSort);
        return categoryMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    public List<CodeCategory> listAll() {
        Long userId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<CodeCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CodeCategory::getUserId, userId)
               .orderByAsc(CodeCategory::getSort);
        return categoryMapper.selectList(wrapper);
    }
}
