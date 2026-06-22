package com.gcsj2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gcsj2.common.PageResult;
import com.gcsj2.common.Result;
import com.gcsj2.dto.CategorySaveDTO;
import com.gcsj2.entity.CodeCategory;
import com.gcsj2.service.CodeCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码分类控制器
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CodeCategoryController {

    private final CodeCategoryService categoryService;

    /**
     * 新增分类
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody CategorySaveDTO dto) {
        categoryService.add(dto);
        return Result.ok("分类创建成功");
    }

    /**
     * 编辑分类
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody CategorySaveDTO dto) {
        categoryService.update(dto);
        return Result.ok("分类修改成功");
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.ok("分类删除成功");
    }

    /**
     * 分页查询分类
     */
    @GetMapping("/page")
    public Result<PageResult<CodeCategory>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        IPage<CodeCategory> page = categoryService.page(current, size);
        return Result.ok(PageResult.from(page));
    }

    /**
     * 查询所有分类（用于下拉框）
     */
    @GetMapping("/all")
    public Result<List<CodeCategory>> listAll() {
        return Result.ok(categoryService.listAll());
    }
}
