package com.olga.aics.controller;

import com.olga.aics.dto.ChatRequest;
import com.olga.aics.service.ChatService;
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
