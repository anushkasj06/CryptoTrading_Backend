package com.zosh.controller;

import com.zosh.config.JwtProvider;
import com.zosh.domain.USER_ROLE;
import com.zosh.exception.UserException;
import com.zosh.model.TwoFactorOTP;
import com.zosh.model.User;
import com.zosh.repository.UserRepository;
import com.zosh.request.LoginRequest;
import com.zosh.response.AuthResponse;
import com.zosh.service.*;
import com.zosh.utils.OtpUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomeUserServiceImplementation customUserDetails;
    @Autowired
    private UserService userService;
    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private VerificationService verificationService;
    @Autowired
    private TwoFactorOtpService twoFactorOtpService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new UserException("Email Is Already Used With Another Account");
        }

        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());
        createdUser.setMobile(user.getMobile());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setRole(USER_ROLE.ROLE_USER); // Default role

        User savedUser = userRepository.save(createdUser);
        watchlistService.createWatchList(savedUser);

        // BUILD AUTHORITIES FROM DB ROLE
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(savedUser.getRole().toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest loginRequest) throws UserException, MessagingException {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findUserByEmail(username);
        String token = JwtProvider.generateToken(authentication);

        if (user.getTwoFactorAuth().isEnabled()) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two factor authentication enabled");
            authResponse.setTwoFactorAuthEnabled(true);

            String otp = OtpUtils.generateOTP();
            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(user.getId());
            if (oldTwoFactorOTP != null) {
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }

            TwoFactorOTP twoFactorOTP = twoFactorOtpService.createTwoFactorOtp(user, otp, token);
            emailService.sendVerificationOtpEmail(user.getEmail(), otp);

            authResponse.setSession(twoFactorOTP.getId());
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        // userDetails.getAuthorities() already contains roles like "ROLE_USER" or "ROLE_ADMIN"
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<AuthResponse> createAdminHandler(@RequestBody User user) throws UserException {
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new UserException("Email Is Already Used With Another Account");
        }

        User admin = new User();
        admin.setEmail(user.getEmail());
        admin.setFullName(user.getFullName());
        admin.setPassword(passwordEncoder.encode(user.getPassword()));
        admin.setRole(USER_ROLE.ROLE_ADMIN); // ENSURE ROLE_ADMIN is set

        User savedAdmin = userRepository.save(admin);
        watchlistService.createWatchList(savedAdmin);

        // BUILD AUTHORITIES EXPLICITLY FOR TOKEN GENERATION
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(savedAdmin.getRole().toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedAdmin.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Admin Register Success");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }
}