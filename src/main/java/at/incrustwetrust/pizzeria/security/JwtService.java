package at.incrustwetrust.pizzeria.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.expires-in-seconds}")
    private long expires;

    public String generateToken(UserDetails userDetails) {
        if (!(userDetails instanceof SecurityUser secUser)) {
            throw new IllegalArgumentException("UserDetails is not SecurityUser – cannot issue token");
        }

        Instant now = Instant.now();

        String email    = secUser.getEmail();            // Login-Identität
        String userId   = secUser.getId();           // DB-ID
        boolean isAdmin = secUser.isAdmin();
        String username = secUser.getDisplayUsername();  // UI-Name

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(email) // sub = Email
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(expires)))
                .withClaim("userId", userId)
                .withClaim("admin", isAdmin)
                .withClaim("username", username)
                .sign(Algorithm.HMAC256(secret));
    }

    // Subject (bei dir = Email)
    public String extractUsername(String token) {
        return verify(token).getSubject();
    }

    // Individuelle Claims
    public String extractUserId(String token) {
        return verify(token).getClaim("userId").asString();
    }

    public boolean extractIsAdmin(String token) {
        return verify(token).getClaim("isAdmin").asBoolean();
    }

    public String extractDisplayUsername(String token) {
        return verify(token).getClaim("username").asString();
    }

    public DecodedJWT verify(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build()
                .verify(token);
    }
}
