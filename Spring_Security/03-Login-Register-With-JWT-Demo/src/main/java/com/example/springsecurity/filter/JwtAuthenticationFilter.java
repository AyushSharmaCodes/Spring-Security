package com.example.springsecurity.filter;

import java.io.IOException;

import com.example.springsecurity.service.UserDetailsServiceImpl;
import com.example.springsecurity.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException; // Import the base exception from JJWT

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor(onConstructor_ =  @Autowired)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // --- 1. Check for JWT Presence ---
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No JWT or incorrect format, let the request pass to the next filter
            // Spring Security will handle paths that don't require auth later
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token (skips "Bearer ")
        jwt = authHeader.substring(7);

        try {
            // --- 2. Extract Username and Load UserDetails ---
            // If the token is structurally invalid, tampered with, or expired,
            // the extractUsername method (which calls extractAllClaims) will throw an exception.
            username = jwtUtil.extractUsername(jwt);

            // Check if username is present and no authentication has been set yet
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load the user from the database/service
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // --- 3. Final Validation and Authentication Setup ---
                if (jwtUtil.validateToken(jwt, userDetails)) {

                    // Token is valid; create authentication object
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Credentials are null for JWT, as the token itself is the credential
                            userDetails.getAuthorities()
                    );

                    // Attach request details (IP, session, etc.) for logging/security audit
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Set the Authentication in the Security Context
                    // This tells Spring Security that the user is authenticated for this request
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (JwtException | IllegalArgumentException e) {
            // --- 4. Robust Error Handling ---
            // Catch all possible JWT validation failures (SignatureException, ExpiredJwtException, etc.)

            // You can log the specific error here:
            // logger.error("JWT validation failed: {}", e.getMessage());

            // Reject the request explicitly by sending a 401 response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            // Optional: Write a specific error message to the response body
            response.getWriter().write("{ \"error\": \"Unauthorized\", \"message\": \"" + e.getMessage() + "\" }");
            return; // STOP the filter chain execution

        } catch (Exception e) {
            // Handle any other unexpected exceptions during UserDetails loading or processing
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{ \"error\": \"Server Error\", \"message\": \"An internal error occurred during authentication.\" }");
            return;
        }

        // --- 5. Continue Chain ---
        filterChain.doFilter(request, response);
    }
}
