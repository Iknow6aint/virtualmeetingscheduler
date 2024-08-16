package com.afretradedtm.virtualmeetingscheduler.controller;

import com.afretradedtm.virtualmeetingscheduler.model.User;
import com.afretradedtm.virtualmeetingscheduler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    // JWT authentication can be added here
}
