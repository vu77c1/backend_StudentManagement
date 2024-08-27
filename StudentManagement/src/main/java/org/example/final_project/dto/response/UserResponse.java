package org.example.final_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserResponse {
    Long userId;
    private List<String> role; // Danh sách vai trò dưới dạng chuỗi
    private String username;
    private String email;
}
