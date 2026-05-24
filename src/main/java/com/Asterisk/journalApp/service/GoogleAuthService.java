package com.Asterisk.journalApp.service;

import com.Asterisk.journalApp.entity.User;
import com.Asterisk.journalApp.repository.UserRepository;
import com.Asterisk.journalApp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class GoogleAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private static final String TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static final String USERINFO_ENDPOINT = "https://oauth2.googleapis.com/tokeninfo";
    private static final String REDIRECT_URI = "http://localhost:8081/journal/public/auth/callback";

    public String handleGoogleLogin(String code) {
        String idToken = exchangeCodeForIdToken(code);
        String email = fetchEmailFromToken(idToken);
        ensureUserExists(email);
        return jwtUtil.generateToken(email);
    }

    private String exchangeCodeForIdToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                TOKEN_ENDPOINT, request, Map.class
        );

        if (tokenResponse.getBody() == null) {
            throw new RuntimeException("Empty response from Google token endpoint");
        }

        return (String) tokenResponse.getBody().get("id_token");
    }

    private String fetchEmailFromToken(String idToken) {
        String userInfoUrl = USERINFO_ENDPOINT + "?id_token=" + idToken;

        ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(
                userInfoUrl, Map.class
        );

        if (userInfoResponse.getBody() == null) {
            throw new RuntimeException("Empty response from Google userinfo endpoint");
        }

        return (String) userInfoResponse.getBody().get("email");
    }

    private void ensureUserExists(String email) {
        try {
            userDetailsService.loadUserByUsername(email);
            log.info("Existing user logged in via Google OAuth: {}", email);
        } catch (Exception e) {
            User user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            log.info("New user registered via Google OAuth: {}", email);
        }
    }
}