// src/main/java/com/zosh/response/RebalanceResponse.java
package com.zosh.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
public class RebalanceResponse {
    private double totalPortfolioValue;
    private List<RebalanceInstruction> instructions;

    @Data
    @AllArgsConstructor
    public static class RebalanceInstruction {
        private String coinId;
        private String action; // "BUY" or "SELL"
        private double quantity;
        private double estimatedValue;
    }
}