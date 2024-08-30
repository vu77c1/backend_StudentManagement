package org.example.final_project.controller;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.dto.response.ResponseError;
import org.example.final_project.service.MailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    private final MailService mailService;

    public CommonController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send-email")
    public ResponseData<?> sendEmail(@RequestParam String recipients, @RequestParam String subject,
                                     @RequestParam String content, @RequestParam(required = false) MultipartFile[] files) {
        log.info("Request GET /common/send-email");
        try {
            return new ResponseData<>(ACCEPTED.value(), mailService.sendEmail(recipients, subject, content, files));
        } catch (UnsupportedEncodingException | MessagingException e) {
            log.error("Sending email was failure, message={}", e.getMessage(), e);
            return new ResponseError(BAD_REQUEST.value(), "Sending email was failure");
        }
    }
}
