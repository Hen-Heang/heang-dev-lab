package com.heang.springmybatistest.service;


import com.heang.springmybatistest.dto.UserListResponse;
import com.heang.springmybatistest.dto.UserRequest;
import com.heang.springmybatistest.dto.UserResponse;
import com.heang.springmybatistest.dto.UserSearchRequest;
import com.heang.springmybatistest.dto.UserUpdateRequest;
import com.heang.springmybatistest.exception.NotFoundException;
import com.heang.springmybatistest.mapper.UserDtoMapper;
import com.heang.springmybatistest.mapper.UserMapper;
import com.heang.springmybatistest.model.Users;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, UserDtoMapper userDtoMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userDtoMapper = userDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(UserRequest userRequest) {
        log.info("Creating user: username={}, email={}", userRequest.getUsername(), userRequest.getEmail());

        if (userRequest.getStatus() != null && !userRequest.getStatus().isBlank()) {
            userRequest.setStatus(userRequest.getStatus().toUpperCase());
        } else {
            userRequest.setStatus(null);
        }
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userMapper.insertUser(userRequest);

        log.info("User created successfully: username={}", userRequest.getUsername());
    }

    @Override
    public UserListResponse searchUsers(UserSearchRequest request) {
        log.debug("Searching users: keyword={}, status={}, page={}, size={}",
                request.getKeyword(), request.getStatus(), request.getPage(), request.getSize());

        normalizeSearchRequest(request);
        List<Users> users = userMapper.searchUsers(request);
        int total = userMapper.countUsers(request);

        log.debug("User search result: total={}", total);

        List<UserResponse> responses = userDtoMapper.toUserResponseList(users);
        int page = request.getPage() != null ? request.getPage() : 1;
        int size = request.getSize() != null ? request.getSize() : users.size();
        return new UserListResponse(responses, total, page, size);
    }

    @Override
    public Users selectUserById(Long id) {
        return null;
    }

    @Override
    public UserResponse updateUser(Long id, @Valid UserUpdateRequest userUpdateRequest) {
        log.info("Updating user: id={}", id);

        if (userUpdateRequest.getStatus() != null && !userUpdateRequest.getStatus().isBlank()) {
            userUpdateRequest.setStatus(userUpdateRequest.getStatus().toUpperCase());
        } else {
            userUpdateRequest.setStatus(null);
        }
        userMapper.updateUser(id, userUpdateRequest);

        Users getUserUpdate = userMapper.selectUserById(id);
        log.info("User updated successfully: id={}", id);
        return userDtoMapper.toUserResponse(getUserUpdate);
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.debug("Finding user by id: {}", id);
        Users user = userMapper.selectUserById(id);
        if (user == null) {
            log.warn("User not found: id={}", id);
            throw new NotFoundException("User not found with id: " + id);
        }
        log.debug("User found: id={}, username={}", user.getId(), user.getUsername());
        return userDtoMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user: id={}", id);
        Users userExist = userMapper.selectUserById(id);
        if (userExist == null) {
            log.warn("Cannot delete — user not found: id={}", id);
            throw new NotFoundException("User not found with id: " + id);
        }
        userMapper.deleteUser(id);
        log.info("User deleted successfully: id={}, username={}", id, userExist.getUsername());
    }

    private void normalizeSearchRequest(UserSearchRequest request) {
        if (request == null) {
            return;
        }
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            request.setStatus(request.getStatus().toUpperCase());
        }
        if (request.getKeyword() != null && request.getKeyword().isBlank()) {
            request.setKeyword(null);
        }
        if (request.getUsername() != null && request.getUsername().isBlank()) {
            request.setUsername(null);
        }
        if (request.getEmail() != null && request.getEmail().isBlank()) {
            request.setEmail(null);
        }
    }
}
