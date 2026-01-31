package com.zosh.service;

import com.zosh.model.Asset;
import com.zosh.model.Order;
import com.zosh.model.User;
import com.zosh.model.Wallet;
import com.zosh.repository.UserRepository;
import com.zosh.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private WalletService walletService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Asset> getUserAssets(Long userId) throws Exception {
        // Reuses the asset logic you already have
        return assetService.getUsersAssets(userId);
    }

    @Override
    public Wallet getUserWallet(Long userId) throws Exception {
        User user = userService.findUserById(userId);
        return walletService.getUserWallet(user);
    }

    @Override
    public List<Order> getAllGlobalOrders() {
        // Fetches every order from the database regardless of user
        return orderRepository.findAll();
    }
}