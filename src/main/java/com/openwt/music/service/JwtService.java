package com.openwt.music.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class JwtService {
    private static final Duration JWT_TOKEN_VALIDITY = Duration.ofMinutes(5);
    private final Algorithm hmac512;
    private final JWTVerifier verifier;
    public JwtService(@Value("${jwt.secret}") final String secret) {
        this.hmac512 = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.hmac512).build();
    }

    public String generateToken(final UserDetails userDetails) {
        final Instant now = Instant.now();
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuer("app")
                .withIssuedAt(now)
                .withExpiresAt(now.plusMillis(JWT_TOKEN_VALIDITY.toMillis()))
                .sign(this.hmac512);
    }

    public String validateTokenAndGetUsername(final String token) {
        try {
            return verifier.verify(token).getSubject();
        } catch (final JWTVerificationException verificationEx) {
            log.warn("token invalid: {}", verificationEx.getMessage());
            return null;
        }
    }
}
