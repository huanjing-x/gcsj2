package com.gcsj2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gcsj2.common.PageResult;
import com.gcsj2.common.Result;
import com.gcsj2.dto.SnippetQueryDTO;
import com.gcsj2.dto.SnippetSaveDTO;
import com.gcsj2.service.CodeSnippetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 代码片段控制器
 */
@RestController
@RequestMapping("/api/snippet")
@RequiredArgsConstructor
public class CodeSnippetController {

    private final CodeSnippetService snippetService;

    /**
     * 新增代码片段
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody SnippetSaveDTO dto) {
        snippetService.add(dto);
        return Result.ok("代码片段创建成功");
    }

    /**
     * 编辑代码片段
     */
    @PutMapping
    public Result<Void> update(@Valid @RequestBody SnippetSaveDTO dto) {
        snippetService.update(dto);
        return Result.ok("代码片段更新成功");
    }

    /**
     * 删除代码片段
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        snippetService.delete(id);
        return Result.ok("代码片段已删除");
    }

    /**
     * 分页查询代码片段（含筛选条件）
     */
    @GetMapping("/page")
    public Result<PageResult<Map<String, Object>>> page(SnippetQueryDTO dto) {
        IPage<Map<String, Object>> page = snippetService.page(dto);
        return Result.ok(PageResult.from(page));
    }

    /**
     * 获取代码片段详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getDetail(@PathVariable Long id) {
        return Result.ok(snippetService.getDetail(id));
    }

    /**
     * 切换收藏状态
     */
    @PutMapping("/{id}/collect")
    public Result<Void> toggleCollect(@PathVariable Long id) {
        snippetService.toggleCollect(id);
        return Result.ok();
    }

    /**
     * 获取代码内容（用于复制）
     */
    @GetMapping("/{id}/content")
    public Result<String> getContent(@PathVariable Long id) {
        return Result.ok(snippetService.getCodeContent(id));
    }
}
