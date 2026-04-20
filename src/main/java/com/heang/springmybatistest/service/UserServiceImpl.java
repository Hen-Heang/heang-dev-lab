package com.heang.springmybatistest.service;


import com.heang.springmybatistest.dto.UserListResponse;
import com.heang.springmybatistest.dto.UserRequest;
import com.heang.springmybatistest.dto.UserResponse;
import com.heang.springmybatistest.dto.UserSearchRequest;
import com.heang.springmybatistest.dto.UserUpdateRequest;
import com.heang.springmybatistest.mapper.UserDtoMapper;
import com.heang.springmybatistest.mapper.UserMapper;
import com.heang.springmybatistest.model.Users;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements  UserService {

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
        if (userRequest.getStatus() != null && !userRequest.getStatus().isBlank()) {
            userRequest.setStatus(userRequest.getStatus().toUpperCase());
        } else {
            userRequest.setStatus(null);
        }
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userMapper.insertUser(userRequest);
    }

    @Override
    public UserListResponse searchUsers(UserSearchRequest request) {
        normalizeSearchRequest(request);
        List<Users> users = userMapper.searchUsers(request);
        int total = userMapper.countUsers(request);

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

//        Update user logic to be implemented
        if (userUpdateRequest.getStatus() != null && !userUpdateRequest.getStatus().isBlank()) {
            userUpdateRequest.setStatus(userUpdateRequest.getStatus().toUpperCase());
        } else {
            userUpdateRequest.setStatus(null);
        }
        userMapper.updateUser(id, userUpdateRequest);

        Users getUserUpdate = userMapper.selectUserById(id);
        return userDtoMapper.toUserResponse(getUserUpdate);
    }

    @Override
    public UserResponse getUserById(Long id) {
        Users user = userMapper.selectUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + id);
        }
            return userDtoMapper.toUserResponse(user);
}

    @Override
    public void deleteUser(Long id) {
        // Delete user logic to be implemented
        Users userExist = userMapper.selectUserById(id);
        if(userExist == null){
            throw new RuntimeException("User not found with id: " + id);

        }
        userMapper.deleteUser(id);
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
