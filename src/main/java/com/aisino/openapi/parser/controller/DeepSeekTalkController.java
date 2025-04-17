package com.aisino.openapi.parser.controller;


import io.github.pigmesh.ai.deepseek.core.DeepSeekClient;
import io.github.pigmesh.ai.deepseek.core.chat.ChatCompletionRequest;
import io.github.pigmesh.ai.deepseek.core.chat.ChatCompletionResponse;
import io.github.pigmesh.ai.deepseek.core.chat.ResponseFormatType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/deepseek")
public class DeepSeekTalkController {

    @Resource
    private DeepSeekClient deepSeekClient;

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatCompletionResponse> chat(@RequestParam("prompt") String prompt,
                                             @RequestParam(value = "model", defaultValue = "deepseek-ai/DeepSeek-R1") String model,
                                             @RequestParam(value = "temperature", defaultValue = "0.7") Double temperature,
                                             @RequestParam(value = "frequencyPenalty", defaultValue = "0.5") Double frequencyPenalty,
                                             @RequestParam(value = "user", defaultValue = "user") String user,
                                             @RequestParam(value = "topP", defaultValue = "0.7") Double topP,
                                             @RequestParam(value = "maxCompletionTokens", defaultValue = "1024") Integer maxCompletionTokens) {
        log.info("prompt: {}", prompt);
        log.info("model: {}, temperature: {}, frequencyPenalty: {}, user: {}, topP: {}, maxCompletionTokens: {}", model, temperature, frequencyPenalty, user, topP, maxCompletionTokens);

        if (!StringUtils.hasText(prompt)) {
            throw new IllegalArgumentException("prompt is empty");
        }

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                // 添加用户输入的提示词（prompt），即模型生成文本的起点。告诉模型基于什么内容生成文本。
                .addUserMessage(prompt)
                // 指定使用的模型名称。不同模型可能有不同的能力和训练数据，选择合适的模型会影响生成结果。
                .model(model)
                // 是否以流式（streaming）方式返回结果。
                .stream(true)
                // 控制生成文本的随机性。0.0：生成结果非常确定，倾向于选择概率最高的词。1.0：生成结果更具随机性和创造性。
                .temperature(temperature)
                // 控制生成文本中重复内容的惩罚程度。0.0：不惩罚重复内容。1.0 或更高：减少重复内容，增加多样性。
                .frequencyPenalty(frequencyPenalty)
                // 标识请求的用户。用于跟踪和日志记录，通常用于区分不同用户的请求。
                .user(user)
                // 控制生成文本时选择词的范围。0.7：从概率最高的 70% 的词中选择。1.0：不限制选择范围。
                .topP(topP)
                // 控制模型生成的文本的最大长度。这对于防止生成过长的文本或确保响应在预期的范围内非常有用。
                .maxCompletionTokens(maxCompletionTokens)
                // 响应结果的格式。
                .responseFormat(ResponseFormatType.TEXT)
                .build();

        return deepSeekClient.chatFluxCompletion(request);
    }
}

