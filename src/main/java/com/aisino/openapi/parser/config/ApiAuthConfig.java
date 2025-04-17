package com.aisino.openapi.parser.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "api.auth")
public class ApiAuthConfig {

    // 跳过验证的路径
    private List<String> skipAuthPaths = new ArrayList<>();

    // 客户端ID和对应的公钥
    private Map<String, String> clientPublicKeys = new HashMap<>();

    //签名算法
    private String  signatureAlgorithm;

    //请求时间戳有效期
    private long timestampValidityMillis;
    public String getClientPublicKey(String clientId) {
        return clientPublicKeys.get(clientId);
    }
}
