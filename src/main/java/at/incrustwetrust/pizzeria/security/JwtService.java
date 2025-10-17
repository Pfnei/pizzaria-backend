package at.incrustwetrust.pizzeria.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
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

    public String generateToken(UserDetails user) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getUsername())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(expires)))
                .withClaim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .sign(Algorithm.HMAC256(secret));
    }

    public String extractUsername(String token) {
        return verify(token).getSubject();
    }

    public DecodedJWT verify(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build()
                .verify(token);
    }
}
