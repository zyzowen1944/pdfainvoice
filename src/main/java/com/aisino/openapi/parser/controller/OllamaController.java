package com.aisino.openapi.parser.controller;

import com.aisino.openapi.parser.service.OllamaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/ollama")
public class OllamaController {

    private final OllamaService ollamaService;

    public OllamaController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @GetMapping("/chat/{msg}")
    public String search(@PathVariable("msg")  String prompt) {
        return ollamaService.search(prompt);
    }
}
