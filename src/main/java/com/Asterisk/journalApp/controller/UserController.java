package com.Asterisk.journalApp.controller;


import com.Asterisk.journalApp.dto.UserDTO;
import com.Asterisk.journalApp.entity.User;
import com.Asterisk.journalApp.repository.UserRepository;
import com.Asterisk.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.Asterisk.journalApp.api.response.WeatherResponse;
import com.Asterisk.journalApp.service.WeatherService;

@RestController
@RequestMapping("/user")
@Tag(name = "User APIs", description = "Read,Update & Delete User")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;


    @Operation(summary = "Update logged in user details")
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Delete logged in user account")
    @DeleteMapping
    public ResponseEntity<?> deleteUserById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get logged in user details with weather info")
    @GetMapping
    public ResponseEntity<String> greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Hyderabad");
        return new ResponseEntity<>("Hi " + authentication.getName() + ", Weather feels like " + weatherResponse.getMain().getFeelsLike(), HttpStatus.OK);
    }
}
