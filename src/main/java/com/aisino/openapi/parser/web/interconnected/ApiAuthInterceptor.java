package com.aisino.openapi.parser.web.interconnected;

import com.aisino.openapi.parser.config.ApiAuthConfig;
import com.aisino.openapi.parser.exception.AuthenticationException;
import com.aisino.openapi.parser.util.webutil.SignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class ApiAuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ApiAuthInterceptor.class);

    @Autowired
    private ApiAuthConfig apiAuthConfig;

    @Autowired
    private SignatureUtil signatureUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否需要跳过验证的路径
        String requestURI = request.getRequestURI();
        if (shouldSkipAuth(requestURI)) {
            return true;
        }

        // 获取请求参数
        Map<String, String> params = getAllParameters(request);

        // 基本参数检查
        checkRequiredParams(params);

        // 检查时间戳，防止重放攻击
        checkTimestamp(params);

        // 检查客户端ID是否合法
        String clientId = params.get("clientId");
        String publicKey = apiAuthConfig.getClientPublicKey(clientId);
        if (publicKey == null) {
            throw new AuthenticationException("无效的客户端ID");
        }

        // 验证签名
        String signature = params.get("signature");
        params.remove("signature");  // 签名验证时需要移除签名参数

        boolean isValid = signatureUtil.verifySignature(
                params,
                signature,
                publicKey,
                apiAuthConfig.getSignatureAlgorithm()
        );

        if (!isValid) {
            logger.warn("API签名验证失败，clientId: {}", clientId);
            throw new AuthenticationException("签名验证失败");
        }

        // 验证通过
        logger.info("API鉴权成功，clientId: {}", clientId);
        return true;
    }

    // 其他方法保持不变...
    private boolean shouldSkipAuth(String requestURI) {
        // 这里可以添加不需要认证的路径
        return apiAuthConfig.getSkipAuthPaths().stream()
                .anyMatch(requestURI::startsWith);
    }

    private Map<String, String> getAllParameters(HttpServletRequest request) throws IOException {
        Map<String, String> params = new HashMap<>();

        // 获取URL参数
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            params.put(name, request.getParameter(name));
        }

        // 如果是POST请求，可能需要解析请求体
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            // 这里可以添加解析请求体的逻辑
            // 例如使用ObjectMapper解析JSON请求体
        }

        return params;
    }

    private void checkRequiredParams(Map<String, String> params) {
        // 检查必要参数
        String[] requiredParams = {"clientId", "timestamp", "nonce", "signature"};
        for (String param : requiredParams) {
            if (!params.containsKey(param) || params.get(param) == null || params.get(param).isEmpty()) {
                throw new AuthenticationException("缺少必要参数: " + param);
            }
        }
    }

    private void checkTimestamp(Map<String, String> params) {
        long timestamp;
        try {
            timestamp = Long.parseLong(params.get("timestamp"));
        } catch (NumberFormatException e) {
            throw new AuthenticationException("时间戳格式错误");
        }

        long currentTime = System.currentTimeMillis();
        long diff = Math.abs(currentTime - timestamp);

        if (diff > apiAuthConfig.getTimestampValidityMillis()) {
            throw new AuthenticationException("请求已过期，请检查客户端时间");
        }
    }
}
