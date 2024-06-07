package com.example.JwtExample.Controller;

import com.example.JwtExample.Entity.User;
import com.example.JwtExample.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok( service.getAllUsers() );
    }
}
