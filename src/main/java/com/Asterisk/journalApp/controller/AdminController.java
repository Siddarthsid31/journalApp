package com.Asterisk.journalApp.controller;

import com.Asterisk.journalApp.cache.AppCache;
import com.Asterisk.journalApp.dto.UserDTO;
import com.Asterisk.journalApp.entity.User;
import com.Asterisk.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs", description = "Admin Operations - Get All Users, Create Admin, Clear Cache")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @Operation(summary = "Get all users in the system")
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> all = userService.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create a new admin user")
    @PostMapping("/create-admin-user")
    public void createUser(@RequestBody UserDTO user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        userService.saveAdmin(newUser);
    }

    @Operation(summary = "Clear the application cache")
    @GetMapping("clear-app-cache")
    public void clearAppCache(){
        appCache.init();
    }
}
