package com.gcsj2.controller;

import com.gcsj2.common.Result;
import com.gcsj2.dto.AiRequestDTO;
import com.gcsj2.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * AI 代码辅助控制器
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * AI 代码处理（添加注释 / BUG检测 / 重构优化）
     */
    @PostMapping("/process")
    public Result<String> processCode(@Valid @RequestBody AiRequestDTO dto) {
        String result = aiService.processCode(dto);
        return Result.ok("处理完成", result);
    }
}
