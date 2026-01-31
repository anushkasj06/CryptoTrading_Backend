package com.zosh.service;

import com.zosh.response.ApiResponse;

public interface ChatBotService {

    ApiResponse getCoinDetails(String prompt);

}
