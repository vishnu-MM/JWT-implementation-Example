package com.example.JwtExample.Services;

import com.example.JwtExample.Config.JwtService;
import com.example.JwtExample.Entity.User;
import com.example.JwtExample.Repository.UserRepository;
import com.example.JwtExample.Utility.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthenticationServiceImpl(UserRepository repository, JwtService jwtService,
                                     PasswordEncoder passwordEncoder, AuthenticationManager authManager) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    @Override
    public AuthenticationResponse register(User user) {
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        User savedUser = repository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(jwtToken,"");
    }

    @Override
    public AuthenticationResponse authenticate(User user) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken( user.getUsername(), user.getPassword() )
        );
        User authenticatedUser = repository.findByUsername( user.getUsername() )
                        .orElseThrow(()-> new UsernameNotFoundException("User with this username is not fount") );
        String jwtToken = jwtService.generateToken( authenticatedUser );
        return new AuthenticationResponse( jwtToken,"" );
    }
}
