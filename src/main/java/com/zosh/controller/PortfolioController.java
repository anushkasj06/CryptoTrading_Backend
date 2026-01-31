// src/main/java/com/zosh/controller/PortfolioController.java
package com.zosh.controller;

import com.zosh.model.User;
import com.zosh.request.RebalanceRequest;
import com.zosh.response.RebalanceResponse;
import com.zosh.service.PortfolioService;
import com.zosh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserService userService;

    @PostMapping("/rebalance-plan")
    public ResponseEntity<RebalanceResponse> getRebalancePlan(
            @RequestHeader("Authorization") String jwt,
            @RequestBody RebalanceRequest request) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        RebalanceResponse res = portfolioService.calculateRebalancePlan(user, request);
        return ResponseEntity.ok(res);
    }
}