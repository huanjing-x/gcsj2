package com.gcsj2.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gcsj2.dto.AiRequestDTO;
import com.gcsj2.exception.BusinessException;
import com.gcsj2.service.AiService;
import com.gcsj2.service.UserConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * AI 服务实现 - 从用户配置中读取 AI 接口信息
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final UserConfigService configService;

    @Override
    public String processCode(AiRequestDTO dto) {
        // 从数据库读取当前用户的 AI 配置
        String apiUrl = configService.getConfig("ai_api_url");
        String apiKey = configService.getConfig("ai_api_key");
        String model = configService.getConfig("ai_model");

        // 未配置 AI 时，返回友好提示而非报错
        if (!StringUtils.hasText(apiUrl)) {
            throw new BusinessException("请先在「系统设置」中配置 AI 接口地址");
        }
        if (!StringUtils.hasText(apiKey)) {
            throw new BusinessException("请先在「系统设置」中配置 AI 接口密钥");
        }
        if (!StringUtils.hasText(model)) {
            model = "deepseek-v4-pro"; // 默认模型
        }

        // 构建发送给 AI 的提示词
        String systemPrompt = buildSystemPrompt(dto.getActionType(), dto.getLanguage());
        String userMessage = "请处理以下代码：\n```" + dto.getLanguage() + "\n" + dto.getCodeContent() + "\n```";

        try {
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.set("model", model);

            JSONArray messages = new JSONArray();

            JSONObject systemMsg = new JSONObject();
            systemMsg.set("role", "system");
            systemMsg.set("content", systemPrompt);
            messages.add(systemMsg);

            JSONObject userMsg = new JSONObject();
            userMsg.set("role", "user");
            userMsg.set("content", userMessage);
            messages.add(userMsg);

            requestBody.set("messages", messages);
            requestBody.set("temperature", 0.3);

            // 发送请求
            log.info("发送 AI 请求，操作类型：{}，语言：{}，模型：{}", dto.getActionType(), dto.getLanguage(), model);

            HttpResponse response = HttpRequest.post(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(requestBody.toString())
                    .timeout(60000)
                    .execute();

            if (response.getStatus() != 200) {
                String errorBody = response.body();
                log.error("AI 接口返回错误，状态码：{}，响应体：{}", response.getStatus(), errorBody);
                throw new BusinessException("AI 服务调用失败：" + errorBody);
            }

            // 解析响应
            JSONObject respJson = JSONUtil.parseObj(response.body());
            JSONArray choices = respJson.getJSONArray("choices");
            if (choices == null || choices.isEmpty()) {
                throw new BusinessException("AI 未返回有效内容");
            }

            JSONObject firstChoice = choices.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            String content = message.getStr("content");

            // 提取代码块中的内容
            return extractCodeBlock(content);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用 AI 接口异常", e);
            throw new BusinessException("AI 服务暂时不可用，请稍后重试");
        }
    }

    /**
     * 根据操作类型构建不同的系统提示词
     */
    private String buildSystemPrompt(String actionType, String language) {
        switch (actionType) {
            case "comment":
                return "你是一名资深程序员。请为以下 " + language + " 代码添加详细的中文注释，"
                        + "解释每段代码的功能和关键逻辑。保持原有代码结构不变，"
                        + "只添加注释。请直接输出带注释的完整代码。";

            case "debug":
                return "你是一名资深代码审查专家。请检查以下 " + language + " 代码中的BUG和潜在问题，"
                        + "给出问题说明和修复后的完整代码。"
                        + "如果代码没有明显问题，也请说明。请以以下格式回复：\n"
                        + "【问题分析】\n...\n【修复后代码】\n```语言\n...\n```";

            case "refactor":
                return "你是一名代码重构专家。请对以下 " + language + " 代码进行精简和重构优化，"
                        + "使其更简洁、更高效、更易读。保持原有功能不变。"
                        + "请直接输出重构后的完整代码，并在代码前用注释简要说明优化点。";

            default:
                return "请处理以下代码。";
        }
    }

    /**
     * 从 AI 回复中提取代码块内容
     */
    private String extractCodeBlock(String content) {
        int start = content.indexOf("```");
        if (start != -1) {
            int end = content.indexOf("```", start + 3);
            if (end != -1) {
                String block = content.substring(start + 3, end);
                int newlineIdx = block.indexOf('\n');
                if (newlineIdx != -1 && newlineIdx < 20) {
                    block = block.substring(newlineIdx + 1);
                }
                return block.trim();
            }
        }
        return content.trim();
    }
}
