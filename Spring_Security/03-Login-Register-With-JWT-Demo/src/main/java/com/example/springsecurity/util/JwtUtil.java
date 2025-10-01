package com.example.springsecurity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // IMPORTANT: This must be a Base64-encoded string of at least 32 bytes (256 bits) for HS256
    @Value("${jwt.secret.key}")
    private String SECRET_BASE64;

    @Value("${jwt.expiration.time}")
    private long JWT_TOKEN_VALIDITY;

    @Value("${jwt.token.issuer}")
    private String TOKEN_ISSUER;

    @Value("${jwt.token.audience}")
    private String TOKEN_AUDIENCE;


    // --- 1. Key Management ---
    /**
     * Retrieves the signing key as a SecretKey object.
     * This is the recommended way for symmetric key (HS256) usage.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_BASE64);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // 💡 Example of adding a custom claim: the user's first authority/role
//        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
//        claims.put("role", role);
        return createToken(claims, userDetails.getUsername());
    }

    // --- 2. Token Creation ---
    /**
     * Creates and signs the JWT with mandatory registered claims.
     */
    public String createToken(Map<String, Object> claims, String subject) {

        long nowMillis = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(TOKEN_ISSUER) // 💡 Robustness: Explicitly set the issuer
                .setAudience(TOKEN_AUDIENCE) // 💡 Robustness: Explicitly set the audience
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(nowMillis + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // --- 3. Token Parsing and Claim Extraction ---

    /**
     * Safely extracts and validates ALL claims. This method is the security gate.
     * It handles expiration, signature, and other structural exceptions.
     */
    private Claims extractAllClaims(String token) throws JwtException {
        // Jwts.parserBuilder() is the non-deprecated way to parse tokens
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .requireIssuer(TOKEN_ISSUER) // 💡 Robustness: Mandate the correct issuer
                .requireAudience(TOKEN_AUDIENCE) // 💡 Robustness: Mandate the correct audience
                .build()
                .parseClaimsJws(token) // This line throws exceptions on failure
                .getBody();
    }

    /**
     * Generic method to safely extract a specific claim using the functional interface.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Utility methods (mostly unchanged as they rely on the robust claim extractor)
    public String extractUsername(String token) throws JwtException {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) throws JwtException {
        return extractClaim(token, Claims::getExpiration);
    }


    // --- 4. Token Validation ---

    /**
     * The primary public validation method.
     * It uses a try-catch block to handle ALL possible JWT exceptions explicitly.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);

            // 1. Username Match Check
            if (!username.equals(userDetails.getUsername())) {
                return false;
            }

            // 2. Structural/Expiration Check (Handled by extractAllClaims)
            // The call to extractUsername() internally calls extractAllClaims(),
            // which will throw an exception if the token is expired, tampered with, or invalid.
            return true;

        } catch (SignatureException e) {
            // Log a signature failure (token tampered with, or wrong secret used)
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Log an expiration failure
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            // Log a malformed token (not in the expected JWS format)
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            // Log unsupported JWT (e.g., using an unsupported algorithm)
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Log illegal argument (e.g., claims string is empty)
            System.err.println("JWT claims string is empty: " + e.getMessage());
        } catch (JwtException e) {
            // Catch-all for any other unexpected JWT exceptions
            System.err.println("An unexpected JWT validation error occurred: " + e.getMessage());
        }

        // If any exception is caught, the token is invalid
        return false;
    }
}
