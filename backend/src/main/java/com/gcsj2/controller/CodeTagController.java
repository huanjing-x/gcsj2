package com.gcsj2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gcsj2.common.PageResult;
import com.gcsj2.common.Result;
import com.gcsj2.dto.TagSaveDTO;
import com.gcsj2.entity.CodeTag;
import com.gcsj2.service.CodeTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码标签控制器
 */
@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class CodeTagController {

    private final CodeTagService tagService;

    /**
     * 新增标签
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody TagSaveDTO dto) {
        tagService.add(dto);
        return Result.ok("标签创建成功");
    }

    /**
     * 编辑标签
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody TagSaveDTO dto) {
        tagService.update(dto);
        return Result.ok("标签修改成功");
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return Result.ok("标签删除成功");
    }

    /**
     * 分页查询标签
     */
    @GetMapping("/page")
    public Result<PageResult<CodeTag>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        IPage<CodeTag> page = tagService.page(current, size);
        return Result.ok(PageResult.from(page));
    }

    /**
     * 查询所有标签（用于下拉框/筛选）
     */
    @GetMapping("/all")
    public Result<List<CodeTag>> listAll() {
        return Result.ok(tagService.listAll());
    }
}
