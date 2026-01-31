package com.zosh.controller;

import com.zosh.request.PromptBody;
import com.zosh.response.ApiResponse;
import com.zosh.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    // 🔹 Simple AI Chat Endpoint
    @PostMapping
    public ResponseEntity<ApiResponse> chat(@RequestBody PromptBody promptBody) {
        ApiResponse response =
                chatBotService.getCoinDetails(promptBody.getPrompt());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}