package com.aisino.openapi.parser.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@ConfigurationProperties(prefix = "invoice.parser")
@Getter
@Setter
public class AppConfig {

    private OcrConfig ocr;
    private ThreadingConfig threading;
    //是否启用硅基API
    private boolean enableSilcom;
    //是否启动本地Ollama
    private boolean enableOllama;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threading.getCorePoolSize());
        executor.setMaxPoolSize(threading.getMaxPoolSize());
        executor.setQueueCapacity(threading.getQueueCapacity());
        executor.setThreadNamePrefix("invoice-parser-");
        executor.initialize();
        return executor;
    }

    @Getter
    @Setter
    public static class OcrConfig {
        private String datapath;
        private String language;
    }

    @Getter
    @Setter
    public static class ThreadingConfig {
        private int corePoolSize;
        private int maxPoolSize;
        private int queueCapacity;
    }
}