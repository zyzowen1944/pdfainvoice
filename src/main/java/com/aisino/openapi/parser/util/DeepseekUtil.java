package com.aisino.openapi.parser.util;
import com.aisino.openapi.parser.model.enums.InvoiceType;
import com.aisino.openapi.parser.service.OllamaService;
import com.aisino.openapi.parser.util.prompt.InvoicePromptGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DeepseekUtil {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String deepSeekApiUrl = "https://api.siliconflow.cn/v1/chat/completions"; // 替换为实际的DeepSeek API URL
    private final String deepSeekApiKey = "sk-aaazkvlcdzzqpftlbmlagtgnuhdpmaybkvqxrrsjgdfqlcsg"; // 替换为您的DeepSeek API密钥
    private final String llmodel = "THUDM/GLM-4-9B-0414";
    //   THUDM/GLM-4-9B-0414

    private static DeepseekUtil deepseekUtil;



    static {
        deepseekUtil = new DeepseekUtil();
    }


    public static DeepseekUtil getInstance(){
        if(deepseekUtil==null){
            return new DeepseekUtil();
        }else{
            return deepseekUtil;
        }

    }

    /**
     * 使用DeepSeek V3处理提取的文本
     * @param extractedText 从PDF提取的原始文本
     * @return DeepSeek处理后的JSON字符串
     */
    public String processWithDeepSeek(InvoiceType invoiceType, String extractedText) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Content-Encoding", "gzip");
            headers.setBearerAuth(deepSeekApiKey);
            String msg = "";

            switch (invoiceType.getDescription()){

                case "增值税专用发票":
                    msg = InvoicePromptGenerator.getSpecialInvoicePrompt();
                    break;
                case "普通发票":
                    msg = InvoicePromptGenerator.getGeneralInvoicePrompt() ;
                    break;
                case "电子发票(增值税专用发票)":
                    msg = InvoicePromptGenerator.getElectronicSpecialInvoicePrompt() ;
                    break;
                case "电子发票(普通发票)":
                    msg = InvoicePromptGenerator.getElectronicGeneralInvoicePrompt() ;
                    break;
                case "不动产租凭":
                    msg = InvoicePromptGenerator.getRealEstateLeasePrompt() ;
                    break;
                case "航空客运":
                    msg = InvoicePromptGenerator.getAirTransportTicketPrompt();
                    break;
                case "货物运输":
                    msg = InvoicePromptGenerator.getCargoTransportPrompt();
                    break;
                case "建筑业发票":
                    msg = InvoicePromptGenerator.getConstructionServicePrompt();
                    break;
                case "旅客运输":
                    msg = InvoicePromptGenerator.getPassengerTransportPrompt();
                    break;
                case "农产品":
                    msg = InvoicePromptGenerator.getAgriculturalProductSalesPrompt();
                    break;
                case "铁路客票":
                    msg = InvoicePromptGenerator.getRailwayTicketPrompt();
                    break;
                default:
                    msg = InvoicePromptGenerator.getElectronicGeneralInvoicePrompt();
            }

            // 构建请求体
            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", msg + extractedText);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model",llmodel); // 使用DeepSeek V3模型
            requestBody.put("messages", new Object[]{message});
            requestBody.put("temperature", 0.1); // 低温度，增加输出确定性
            requestBody.put("max_tokens", 2000);


            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送请求到DeepSeek API
            Map<String, Object> response = restTemplate.postForObject(
                    deepSeekApiUrl,
                    requestEntity,
                    Map.class
            );

            // 从响应中提取JSON
            if (response != null && response.containsKey("choices")) {
                ArrayList choices = (ArrayList) response.get("choices");
                if (choices.size() > 0) {
                    Map<String, Object> choice = (Map<String, Object>) choices.get(0);
                    Map<String, Object> message_response = (Map<String, Object>) choice.get("message");
                    String content = (String) message_response.get("content");

                    // 从返回内容中提取JSON部分
                    int jsonStart = content.indexOf("{");
                    int jsonEnd = content.lastIndexOf("}");

                    if (jsonStart >= 0 && jsonEnd >= 0) {
                        return content.substring(jsonStart, jsonEnd + 1);
                    }
                }
            }

            throw new RuntimeException("DeepSeek API没有返回有效的JSON响应");
        } catch (Exception e) {
            log.error("DeepSeek API处理失败: {}", e.getMessage(), e);
            // 如果DeepSeek处理失败，返回一个基本的JSON结构，后续可以通过常规方法填充
            return "{\"invoice\":{\"invoiceType\":\"\",\"invoiceNumber\":\"\",\"invoiceDate\":\"\"}}";
        }
    }

}
