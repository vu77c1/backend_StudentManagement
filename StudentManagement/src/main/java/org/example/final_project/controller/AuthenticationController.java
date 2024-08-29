package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.SignInRequest;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.dto.response.TokenResponse;
import org.example.final_project.service.auth.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;


@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/access-token")
    public ResponseEntity<TokenResponse> accessToken(@RequestBody SignInRequest request) {
        return new ResponseEntity<>(authenticationService.accessToken(request), OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.refreshToken(request), OK);
    }

    @GetMapping("/get-user-token")
    public ResponseEntity<ResponseData<?>> getUserValidToken(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.getUserIsValidToken(request), OK);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.logout(request), OK);
    }

//    @PostMapping("/remove-token")
//    public ResponseEntity<String> removeToken(HttpServletRequest request) {
//        return new ResponseEntity<>(authenticationService.removeToken(request), OK);
//    }
//
//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
//        return new ResponseEntity<>(authenticationService.forgotPassword(email), OK);
//    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> resetPassword(@RequestBody String secretKey) {
//        return new ResponseEntity<>(authenticationService.resetPassword(secretKey), OK);
//    }
//
//    @PostMapping("/change-password")
//    public ResponseEntity<String> changePassword(@RequestBody @Valid ResetPasswordDTO request) {
//        return new ResponseEntity<>(authenticationService.changePassword(request), OK);
//    }
}