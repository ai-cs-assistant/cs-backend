package com.olga.ai.test1.controller;

import com.olga.ai.test1.dto.ChatRequest;
import com.olga.ai.test1.dto.ChatRequest;
import com.olga.ai.test1.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public String chat(@RequestBody ChatRequest request) throws Exception {
        return chatService.ask(request.getMessage());
    }
}
