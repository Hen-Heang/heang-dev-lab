package com.heang.springmybatistest.service;

import com.heang.springmybatistest.mapper.UserMapper;
import com.heang.springmybatistest.model.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserDetailsServiceImpl — Spring Security bridge to our DB (보안 사용자 조회 서비스)
 * <p>
 * How Spring Security uses this:
 *   1. User submits POST /login with username + password
 *   2. Spring Security calls loadUserByUsername(username)
 *   3. This method loads the Users record from DB via MyBatis
 *   4. Returns a UserDetails object Spring Security uses to verify the password
 *   5. If password matches → session is created → the user is logged in
 * <p>
 * The password in DB is BCrypt hash — Spring Security compares automatically
 * using the PasswordEncoder bean defined in SecurityConfig.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        log.debug("Login attempt for username: {}", username);

        // 1. Load user from DB (MyBatis query)
        Users user = userMapper.selectUserByUsername(username);

        // 2. User isn't found → login fails with "Bad credentials"
        if (user == null) {
            log.warn("Login failed — user not found: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // 3. Check account status — Korean systems block non-active accounts
        //    INACTIVE / SUSPENDED / PENDING users cannot log in
        if (!"ACTIVE".equals(user.getStatus())) {
            log.warn("Login failed — account not active: {} status={}", username, user.getStatus());
            throw new UsernameNotFoundException("Account is not active: " + user.getStatus());
        }

        // 4. Build Spring Security UserDetails
        //    authorities = the role string from DB e.g. "ROLE_ADMIN", "ROLE_USER"
        //    Spring Security uses this for @PreAuthorize("hasRole('ADMIN')") checks
        return new User(
            user.getUsername(),
            user.getPassword(),                                  // BCrypt hash — Security verifies it
            List.of(new SimpleGrantedAuthority(user.getRole())) // "ROLE_ADMIN" → authority
        );
    }
}
