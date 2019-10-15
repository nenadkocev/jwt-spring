package kocev.nenad.jwt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for creating and validating JWT token.
 */
@Component
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private JwtSecret jwtSecret;

    public JwtTokenUtil(JwtSecret jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    /**
     * Generates token for user.
     *
     * @param userDetails user for which token will be generated
     * @return generated token
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Validates token for user.
     *
     * @param userDetails user for whom the token is
     * @param token token to be validated
     * @return true if token is valid
     */
    public boolean validateToken(UserDetails userDetails, String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getSecret())
                .parseClaimsJws(token)
                .getBody();
        String userName = claims.getSubject();
        Date expirationDate = claims.getExpiration();
        return userDetails.getUsername().equals(userName) &&
                expirationDate.after(new Date());
    }

    /**
     * Generates token with expiration date, issued date and unique subject.
     *
     * @param claims payload with claims
     * @param subject token's subject
     * @return
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * JWT_TOKEN_VALIDITY))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getSecret())
                .compact();
    }
}
