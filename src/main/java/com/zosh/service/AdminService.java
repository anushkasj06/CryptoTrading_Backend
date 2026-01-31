package com.zosh.service;

import com.zosh.model.Asset;
import com.zosh.model.Order;
import com.zosh.model.User;
import com.zosh.model.Wallet;
import java.util.List;

public interface AdminService {

    // User Management
    List<User> getAllUsers();

    // Portfolio Tracking
    List<Asset> getUserAssets(Long userId) throws Exception;

    Wallet getUserWallet(Long userId) throws Exception;

    // Global Monitoring
    List<Order> getAllGlobalOrders();
}