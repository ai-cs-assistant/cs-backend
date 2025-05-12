package com.olga.aics.handler;

import com.olga.aics.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class AiChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String userMessage = message.getPayload();

        // 呼叫 OpenAPI / AI 模型取得回應
        String aiReply = null;
        try {
            aiReply = callAiApi(userMessage);
        } catch (Exception e) {
            aiReply = "AI 休眠中，請稍後在嘗試";
        }

        session.sendMessage(new TextMessage(aiReply));
    }

    private String callAiApi(String userMessage) throws Exception {
        return chatService.ask(userMessage);
    }
}
