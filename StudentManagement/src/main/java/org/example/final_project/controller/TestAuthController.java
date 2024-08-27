package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testauth")
@Validated
@Slf4j
public class TestAuthController {
    @PreAuthorize("hasRole('SYSADMIN') or hasRole('ADMIN')")
    @PostMapping
    public String testAuth() {
        return "test";
    }
}
