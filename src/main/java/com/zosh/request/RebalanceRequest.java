// src/main/java/com/zosh/request/RebalanceRequest.java
package com.zosh.request;

import lombok.Data;
import java.util.Map;

@Data
public class RebalanceRequest {
    // Key: CoinId (e.g., "bitcoin"), Value: Target Percentage (e.g., 0.50 for 50%)
    private Map<String, Double> targetAllocations;
}