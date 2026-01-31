package com.zosh.service;

import com.zosh.model.*;
import com.zosh.request.RebalanceRequest;
import com.zosh.response.RebalanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PortfolioService {

    @Autowired
    private AssetService assetService;

    @Autowired
    private CoinService coinService;

    public RebalanceResponse calculateRebalancePlan(User user, RebalanceRequest request) throws Exception {
        List<Asset> currentAssets = assetService.getUsersAssets(user.getId()); // Fetches only what you bought
        double totalPortfolioValue = 0;

        // 1. Calculate Total Value of currently owned assets
        for (Asset asset : currentAssets) {
            if (asset.getCoin() != null) {
                totalPortfolioValue += (asset.getQuantity() * asset.getCoin().getCurrentPrice()); //
            }
        }

        List<RebalanceResponse.RebalanceInstruction> instructions = new ArrayList<>();

        // 2. Process Target Allocations requested by the user
        for (Map.Entry<String, Double> entry : request.getTargetAllocations().entrySet()) {
            String coinId = entry.getKey();
            double targetPercent = entry.getValue();
            double targetValue = totalPortfolioValue * targetPercent;

            // Find if the user already owns this coin
            Asset currentAsset = currentAssets.stream()
                    .filter(a -> a.getCoin().getId().equals(coinId))
                    .findFirst().orElse(null);

            double currentQuantity = (currentAsset != null) ? currentAsset.getQuantity() : 0;
            double currentValue = (currentAsset != null) ?
                    currentQuantity * currentAsset.getCoin().getCurrentPrice() : 0;

            double differenceUsd = targetValue - currentValue;

            // 3. Generate suggestion if the gap is larger than $1.00
            if (Math.abs(differenceUsd) > 1.0) {
                Coin coin = coinService.findById(coinId); // Ensure coin details are fresh
                String action = differenceUsd > 0 ? "BUY" : "SELL";
                double tradeQuantity = Math.abs(differenceUsd / coin.getCurrentPrice());

                instructions.add(new RebalanceResponse.RebalanceInstruction(
                        coinId,
                        action,
                        tradeQuantity,
                        Math.abs(differenceUsd)
                ));
            }
        }

        RebalanceResponse response = new RebalanceResponse();
        response.setTotalPortfolioValue(totalPortfolioValue);
        response.setInstructions(instructions);
        return response;
    }
}