package com.Asterisk.journalApp.controller;

import com.Asterisk.journalApp.service.GoogleAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/auth")
@Tag(name = "Public APIs")
@Slf4j
public class GoogleAuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    @Operation(summary = "Handle Google OAuth2 callback and return JWT")
    @GetMapping("/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code) {
        try {
            String jwt = googleAuthService.handleGoogleLogin(code);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            log.error("Exception in handleGoogleCallback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}