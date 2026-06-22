package com.gcsj2.service;

import com.gcsj2.dto.AiRequestDTO;

/**
 * AI 服务接口
 */
public interface AiService {

    /**
     * AI 代码处理
     * @param dto 包含代码内容和操作类型
     * @return AI 处理后的结果文本
     */
    String processCode(AiRequestDTO dto);
}
