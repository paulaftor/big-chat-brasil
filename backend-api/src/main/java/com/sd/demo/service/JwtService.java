package com.sd.demo.service;
import com.sd.demo.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;  // Remover o final da variável, pois o Spring gerencia a injeção da chave secreta
    private final long expirationTime = 86400000; // 24 horas

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .setId(String.valueOf(usuario.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extrairUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValido(String token) {
        return !isTokenExpirado(token);
    }

    private boolean isTokenExpirado(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
