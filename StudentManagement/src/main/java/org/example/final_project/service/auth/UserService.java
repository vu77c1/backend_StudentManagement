package org.example.final_project.service.auth;


import org.example.final_project.dto.request.UserRequestDTO;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.dto.response.UserDetailResponse;
import org.example.final_project.model.auth.User;
import org.example.final_project.util.UserStatus;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  {
    UserDetailsService userDetailsService();

    User getByUsername(String userName);

    long saveUser(UserRequestDTO request);

    long saveUser(User user);

    void updateUser(long userId, UserRequestDTO request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsers(int pageNo, int pageSize);

    List<String> getAllRolesByUserId(long userId);

    User getUserByEmail(String email);
}
