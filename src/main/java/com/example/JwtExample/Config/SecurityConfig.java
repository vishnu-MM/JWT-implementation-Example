package com.example.JwtExample.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final CustomUserDetailsImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig( CustomUserDetailsImpl userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter ) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequestReg -> authRequestReg
                        .requestMatchers("/login/**", "/user-registration/**")
                        .permitAll()
                        .requestMatchers("/admin_only/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .userDetailsService( userDetailsService )
                .sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .addFilterBefore( jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class )
                .build();
    }   

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration authConfig ) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}