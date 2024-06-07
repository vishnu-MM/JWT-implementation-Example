package com.example.JwtExample.Config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor //! This will create constructor for DI
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsImpl userDetailsService;

    @Override
    protected void doFilterInternal( HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userName;

        if ( authHeader == null || !authHeader.startsWith("Bearer ") ) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring( 7 );
        userName = jwtService.extractUsername( jwtToken );

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
            UserDetails user = userDetailsService.loadUserByUsername(userName);

            if ( jwtService.isTokenValid( jwtToken, user ) ) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  user, null, user.getAuthorities()
                );
                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
                SecurityContextHolder.getContext().setAuthentication( authToken );
            }
        }
        filterChain.doFilter(request, response);
    }
}