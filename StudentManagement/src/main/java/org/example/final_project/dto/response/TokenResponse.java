package org.example.final_project.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private List<String> role; // Danh sách vai trò dưới dạng chuỗi
    private String username;
    private String email;
}
