// src/main/java/com/zosh/controller/CommunityChatController.java
package com.zosh.controller;

import com.zosh.model.CommunityMessage;
import com.zosh.model.User;
import com.zosh.repository.CommunityMessageRepository;
import com.zosh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CommunityChatController {

    @Autowired
    private CommunityMessageRepository messageRepository;

    @Autowired
    private UserService userService;

    // Triggered when a user sends a message to /app/chat
    @MessageMapping("/chat")
    @SendTo("/topic/community")
    public CommunityMessage sendMessage(@Payload CommunityMessage message) throws Exception {
        message.setTimestamp(LocalDateTime.now());
        // Save to DB for chat history
        return messageRepository.save(message);
    }

    // Endpoint to load chat history when a user joins
    @GetMapping("/api/community/messages")
    public List<CommunityMessage> getChatHistory() {
        return messageRepository.findAll();
    }
}