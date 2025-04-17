package com.aisino.openapi.parser.util.webutil;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class SignatureUtil {

    /**
     * 验证签名
     *
     * @param params    请求参数（不包含签名）
     * @param signature 签名字符串
     * @param publicKeyStr 公钥字符串（Base64编码）
     * @return 验证结果
     */
    public boolean verifySignature(Map<String, String> params, String signature, String publicKeyStr,String algorithm) {
        try {
            // 按参数名ASCII码升序排序
            SortedMap<String, String> sortedParams = new TreeMap<>(params);

            // 拼接参数
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }

            // 去掉最后的&
            String paramStr = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";

            // 验证签名
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Signature signatureInstance = Signature.getInstance("SHA256withRSA");
            signatureInstance.initVerify(publicKey);
            signatureInstance.update(paramStr.getBytes(StandardCharsets.UTF_8));

            return signatureInstance.verify(Base64.getDecoder().decode(signature));
        } catch (Exception e) {
            // 记录验证失败的原因
            return false;
        }
    }
}
