package com.example.JwtExample.Config;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.JwtExample.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "26b507484ca8b01708b28232f66b8cef858b7b34478183aae7564a0bd51c8c4e";

    public JwtService() {
    }

    public String generateToken(User user ) {
        //todo Map<String, Object> extraClaims = new HashMap<>();
        return Jwts
            .builder()
            //todo .claims().empty().add(extraClaims).and()
            .subject( user.getUsername() )
            .issuedAt( new Date( System.currentTimeMillis() ) )
            .expiration( new Date( System.currentTimeMillis() + 24*60*60*1000 ) )
            .signWith( getSignInKey() )
            .compact();
    }

    public boolean isTokenValid( String jwtToken, UserDetails user ) {
        String username = extractUsername(jwtToken);
        Boolean isValid = username.equals(user.getUsername()) && !isTokenExpired(jwtToken);
        System.out.println(isValid);
        return isValid;
    }

    public String extractUsername( String jwtToken ) {
        return extractClaims(jwtToken, Claims::getSubject);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpirationTime(jwtToken).before(new Date());
    }

    private Date extractExpirationTime(String jwtToken) {
        return extractClaims(jwtToken, Claims::getExpiration);
    }

    private <T>T extractClaims( String jwtToken, Function<Claims, T> claimsResolverFunction ) {
        Claims claims =extractAllClaims(jwtToken);
        return claimsResolverFunction.apply(claims);
    }

    private Claims extractAllClaims( String jwtToken ) {
        return Jwts
                .parser()
                .verifyWith( getSignInKey() )
                .build()
                .parseSignedClaims( jwtToken )
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode( SECRET_KEY );
        return Keys.hmacShaKeyFor(keyBytes);
    }
}