package com.gcsj2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gcsj2.dto.TagSaveDTO;
import com.gcsj2.entity.CodeTag;
import com.gcsj2.entity.SnippetTagRel;
import com.gcsj2.exception.BusinessException;
import com.gcsj2.mapper.CodeTagMapper;
import com.gcsj2.mapper.SnippetTagRelMapper;
import com.gcsj2.security.UserContext;
import com.gcsj2.service.CodeTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 代码标签服务实现
 */
@Service
@RequiredArgsConstructor
public class CodeTagServiceImpl implements CodeTagService {

    private final CodeTagMapper tagMapper;
    private final SnippetTagRelMapper relMapper;

    @Override
    @Transactional
    public void add(TagSaveDTO dto) {
        Long userId = UserContext.getCurrentUserId();

        // 检查标签名是否已存在
        LambdaQueryWrapper<CodeTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CodeTag::getUserId, userId)
               .eq(CodeTag::getTagName, dto.getTagName());
        if (tagMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("标签名已存在");
        }

        CodeTag tag = new CodeTag();
        tag.setUserId(userId);
        tag.setTagName(dto.getTagName());
        tagMapper.insert(tag);
    }

    @Override
    @Transactional
    public void update(TagSaveDTO dto) {
        Long userId = UserContext.getCurrentUserId();
        CodeTag tag = tagMapper.selectById(dto.getId());
        if (tag == null || !tag.getUserId().equals(userId)) {
            throw new BusinessException("标签不存在");
        }

        // 检查新标签名是否与其他标签重复
        LambdaQueryWrapper<CodeTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CodeTag::getUserId, userId)
               .eq(CodeTag::getTagName, dto.getTagName())
               .ne(CodeTag::getId, dto.getId());
        if (tagMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("标签名已存在");
        }

        tag.setTagName(dto.getTagName());
        tagMapper.updateById(tag);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long userId = UserContext.getCurrentUserId();
        CodeTag tag = tagMapper.selectById(id);
        if (tag == null || !tag.getUserId().equals(userId)) {
            throw new BusinessException("标签不存在");
        }

        // 删除关联关系
        LambdaQueryWrapper<SnippetTagRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SnippetTagRel::getTagId, id);
        relMapper.delete(wrapper);

        // 删除标签
        tagMapper.deleteById(id);
    }

    @Override
    public IPage<CodeTag> page(int current, int size) {
        Long userId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<CodeTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CodeTag::getUserId, userId)
               .orderByDesc(CodeTag::getCreateTime);
        return tagMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    public List<CodeTag> listAll() {
        Long userId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<CodeTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CodeTag::getUserId, userId)
               .orderByDesc(CodeTag::getCreateTime);
        return tagMapper.selectList(wrapper);
    }
}
