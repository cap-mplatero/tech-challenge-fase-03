package br.com.fiap.techchallenge.userservice.infrastructure.adapters.security;

import br.com.fiap.techchallenge.userservice.application.ports.output.TokenServicePort;
import br.com.fiap.techchallenge.userservice.domain.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JwtServiceAdapter implements TokenServicePort {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Override
    public String generateToken(User user){

        return Jwts.builder()

                .setSubject(user.getUsername())

                .claim("userId",user.getId())

                .claim("roles",user.getRoles())

                .setExpiration(new Date(System.currentTimeMillis()+expiration))

                .signWith(SignatureAlgorithm.HS256,secret)

                .compact();

    }

    @Override
    public Long getUserIdFromToken(String token){

        Claims claims=Jwts.parser()

                .setSigningKey(secret)

                .parseClaimsJws(token)

                .getBody();

        return claims.get("userId",Long.class);

    }

}