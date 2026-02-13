package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Autowired
    private KeyPair jwtKeyPair;

    private static final long JWT_EXPIRATION = 3_600_000L;

    /**
     * Extrae el nombre de usuario (claim "sub") del token.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrae todos los claims (payload) del token JWT.
     *
     * Verifica la firma con la clave pública.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtKeyPair.getPublic()) // Verificación con PUBLIC KEY
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Genera un token JWT firmado con la clave privada (RS256).
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(jwtKeyPair.getPrivate(), Jwts.SIG.RS256) // Firma RSA
                .compact();
    }

    /**
     * Valida un token JWT verificando:
     * 1) Firma RSA válida
     * 2) Subject coincide
     * 3) No está expirado
     */
    public boolean validateToken(String token, String username) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwtKeyPair.getPublic())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenUsername = claims.getSubject();
            if (tokenUsername == null || !tokenUsername.equals(username)) {
                return false;
            }

            return !isTokenExpired(claims);

        } catch (Exception e) {
            return false; // Firma inválida, token manipulado, expirado, etc.
        }
    }

    /**
     * Verifica si el token ha expirado.
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
