    package com.zosh.controller;

    import com.zosh.model.Asset;
    import com.zosh.model.Order;
    import com.zosh.model.User;
    import com.zosh.model.Wallet;
    import com.zosh.service.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/admin")
    public class AdminController {

        @Autowired
        private UserService userService;

        @Autowired
        private AssetService assetService;

        @Autowired
        private WalletService walletService;

        @Autowired
        private OrderService orderService;

        // Get all registered users
        @GetMapping("/users")
        public ResponseEntity<List<User>> getAllUsers() {
            // You would typically add a check here to ensure the requester is an admin
            // though the SecurityConfig should handle it.
            List<User> users = userService.findAllUsers();
            return ResponseEntity.ok(users);
        }

        // Track a specific user's portfolio
        @GetMapping("/users/{userId}/assets")
        public ResponseEntity<List<Asset>> getUserAssetsForAdmin(@PathVariable Long userId) {
            List<Asset> assets = assetService.getUsersAssets(userId);
            return ResponseEntity.ok(assets);
        }

        // View user's wallet balance
        @GetMapping("/users/{userId}/wallet")
        public ResponseEntity<Wallet> getUserWalletForAdmin(@PathVariable Long userId) throws Exception {
            User user = userService.findUserById(userId);
            Wallet wallet = walletService.getUserWallet(user);
            return ResponseEntity.ok(wallet);
        }

        // View all orders in the system
        @GetMapping("/orders")
        public ResponseEntity<List<Order>> getAllOrders() {
            // You might need to implement a findAll method in OrderService
            return ResponseEntity.ok(orderService.getAllOrders());
        }
    }