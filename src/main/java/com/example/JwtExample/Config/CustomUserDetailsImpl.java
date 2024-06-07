package com.example.JwtExample.Config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.JwtExample.Repository.UserRepository;

@Service
public class CustomUserDetailsImpl implements UserDetailsService {

    private final UserRepository repository;

    CustomUserDetailsImpl( UserRepository repository ) { 
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        return repository.findByUsername( username )
                .orElseThrow(() -> new UsernameNotFoundException("User name not fount"));
    }
}