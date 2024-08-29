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
import org.example.final_project.model.auth.*;
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

import static org.example.final_project.util.TokenType.*;
import static org.springframework.http.HttpHeaders.REFERER;

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


    public TokenResponse refreshToken(HttpServletRequest request) {
        log.info("---------- refreshToken ----------");

        final String refreshToken = request.getHeader(REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }
        final String userName = jwtService.extractUsername(refreshToken, REFRESH_TOKEN);
        var user = userService.getByUsername(userName);
        if (!jwtService.isValid(refreshToken, REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Not allow access with this token");
        }

        // create new access token
        String accessToken = jwtService.generateToken(user);

        // save token to db
        tokenService.save(Token.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
//        redisTokenService.save(RedisToken.builder().id(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
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

    /**
     * Logout
     *
     * @param request
     * @return
     */
    public String logout(HttpServletRequest request) {
        log.info("---------- logout ----------");

        final String token = request.getHeader(REFERER);
        if (StringUtils.isBlank(token)) {
            throw new InvalidDataException("Token must be not blank");
        }

        final String userName = jwtService.extractUsername(token, ACCESS_TOKEN);
        tokenService.delete(userName);

        return "Deleted!";
    }
}
