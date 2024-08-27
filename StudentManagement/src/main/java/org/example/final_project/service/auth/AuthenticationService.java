package org.example.final_project.service.auth;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.final_project.dto.request.SignInRequest;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.dto.response.TokenResponse;
import org.example.final_project.dto.response.UserDetailResponse;
import org.example.final_project.dto.response.UserResponse;
import org.example.final_project.exception.InvalidDataException;
import org.example.final_project.model.auth.Role;
import org.example.final_project.model.auth.Token;
import org.example.final_project.model.auth.User;
import org.example.final_project.model.auth.UserHasRole;
import org.example.final_project.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.final_project.util.TokenType.REFRESH_TOKEN;
import static org.example.final_project.util.TokenType.RESET_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final TokenService tokenService;


    public TokenResponse accessToken(SignInRequest signInRequest) {
        log.info("---------- accessToken ----------");

        var user = userService.getByUsername(signInRequest.getUsername());
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }

        List<String> roles = userService.getAllRolesByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();


        // Xác thực người dùng
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername()
                , signInRequest.getPassword(), authorities));


        // Chuyển đổi danh sách UserHasRole thành danh sách tên vai trò
//        List<String> roles = user.getRoles().stream()
//                .map(UserHasRole::getRole)   // Lấy đối tượng Role từ UserHasRole
//                .map(Role::getName)          // Lấy tên của Role
//                .collect(Collectors.toList());

        // Tạo token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        //lưu token vào database
        tokenService.save(Token.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken).build());


        // Trả về đối tượng TokenResponse
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(roles) // Danh sách vai trò
                .build();
    }

    public ResponseData<UserResponse> getUserIsValidToken(HttpServletRequest httpServletRequest) {
        log.info("Get user info from token request received");
        String token = httpServletRequest.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Lấy phần sau "Bearer "
        } else {
            throw new InvalidDataException("Invalid token format");
        }
        // validate token and user
        Optional<Token> tokenOptional = tokenService.findByAccessToken(token);
        if (tokenOptional.isEmpty()) {
            throw new InvalidDataException("Invalid token");
        }

        var user = userService.getByUsername(tokenOptional.get().getUsername());

        UserResponse userResponse = UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(userService.getAllRolesByUserId(user.getId()))
                .build();


        return new ResponseData<>(HttpStatus.OK.value(), "Get user info success", userResponse);

    }


    public TokenResponse refreshToken(HttpServletRequest httpServletRequest) {
        log.info("Refresh token request received");
        log.info("Refresh token 111 {}", httpServletRequest.getHeader("x-token"));
        String token = httpServletRequest.getHeader("x-token");
        log.debug("Received token: {}", token); // Add this line

        if (StringUtils.isBlank(token)) {
            throw new InvalidDataException("token must be not blank");
        }
        final String username = jwtService.extractUsername(token, REFRESH_TOKEN);
        log.info("username: {}", username);
        return null;
    }


    private User validateToken(String token) {
        // validate token
        var userName = jwtService.extractUsername(token, RESET_TOKEN);

        // check token
        tokenService.isExists(userName);

        // validate user is active or not
        var user = userService.getByUsername(userName);
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }
        return user;
    }
}
