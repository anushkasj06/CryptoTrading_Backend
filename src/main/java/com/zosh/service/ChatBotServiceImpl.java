package com.zosh.service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.zosh.response.ApiResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    // 🔑 Gemini API Key
    private static final String GEMINI_API_URL = "AIzaSyBmLW9F-Xx1i-5kDgANoaSKJ9oTIdo3ENY";

    // 🔗 Gemini Endpoint
    private static final String GEMINI_API_BASE =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_URL;


    @Override
    public ApiResponse getCoinDetails(String prompt) {

        // 🎯 Prompt Engineering (System Prompt)
        String engineeredPrompt =
                "You are an AI assistant for a professional cryptocurrency trading platform.\n" +
                        "Your tone is polite, professional, and helpful.\n" +
                        "Follow these rules strictly:\n" +
                        "1. Greet the user briefly.\n" +
                        "2. Answer clearly in short bullet points.\n" +
                        "3. Avoid unnecessary explanations.\n" +
                        "4. If the question is unclear, politely ask for clarification.\n\n" +
                        "User Question:\n" +
                        prompt;

        String reply = callGemini(engineeredPrompt);
        return new ApiResponse(reply);
    }

    // 🔹 Core Gemini Call
    private String callGemini(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject textPart = new JSONObject().put("text", prompt);
        JSONArray parts = new JSONArray().put(textPart);
        JSONObject content = new JSONObject().put("parts", parts);
        JSONArray contents = new JSONArray().put(content);

        JSONObject requestBody = new JSONObject();
        requestBody.put("contents", contents);

        HttpEntity<String> request =
                new HttpEntity<>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(GEMINI_API_BASE, request, String.class);

            ReadContext ctx = JsonPath.parse(response.getBody());
            return ctx.read("$.candidates[0].content.parts[0].text");

        } catch (Exception e) {
            e.printStackTrace(); // 👈 keep this for debugging
            return "⚠️ Sorry, I'm unable to respond right now. Please try again later.";
        }
    }

}