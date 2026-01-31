package com.zosh.service;

import com.zosh.model.User;
import com.zosh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomeUserServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("user not found with email " + username);
        }

        // Convert the Enum role (e.g., ROLE_ADMIN) into a GrantedAuthority list
        // Since your enum values are already "ROLE_ADMIN" and "ROLE_USER",
        // toString() works perfectly.
        List<GrantedAuthority> authorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(user.getRole().toString());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities // These authorities will be used by JwtProvider to build the token
        );
    }
}