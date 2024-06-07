package com.example.JwtExample.Services;

import com.example.JwtExample.Entity.User;
import com.example.JwtExample.Utility.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register( User user );
    AuthenticationResponse authenticate(User user );
}