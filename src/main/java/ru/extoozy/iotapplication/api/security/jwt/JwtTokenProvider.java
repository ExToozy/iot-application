package ru.extoozy.iotapplication.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;
    private SecretKey key;


    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String getClaim(String token, String claim) {
        return getSignedClaims(token)
                .getPayload()
                .get(claim)
                .toString();
    }

    public boolean isExpiredToken(String token) {
        return getSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    private Jws<Claims> getSignedClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    public String createAccessToken(Long userId, String username) {
        Claims claims = getClaims(userId, username, jwtProperties.getAccess());
        return createToken(claims);

    }

    public String createRefreshToken(Long userId, String username) {
        Claims claims = getClaims(userId, username, jwtProperties.getRefresh());
        return createToken(claims);

    }

    private String createToken(Claims claims) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .signWith(key)
                .compact();
    }

    private Claims getClaims(Long userId, String username, Duration expiration) {
        return Jwts
                .claims()
                .subject(username)
                .add("id", userId)
                .expiration(Date.from(Instant.now().plusMillis(expiration.toMillis())))
                .build();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return getSignedClaims(token).getPayload().getSubject();
    }
}
