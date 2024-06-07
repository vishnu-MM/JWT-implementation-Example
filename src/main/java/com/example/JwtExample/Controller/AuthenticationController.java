package com.example.JwtExample.Controller;

import com.example.JwtExample.Entity.User;
import com.example.JwtExample.Services.AuthenticationService;
import com.example.JwtExample.Utility.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService service;
    @Autowired
    public AuthenticationController( AuthenticationService service ) {
        this.service = service;
    }

    @PostMapping("/user-registration")
    public ResponseEntity<AuthenticationResponse> userRegistration( @RequestBody User newUser ) {
        return new ResponseEntity<>( service.register(newUser), HttpStatus.OK );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login( @RequestBody User user ) {
        return new ResponseEntity<>( service.authenticate( user ), HttpStatus.OK );
    }

}