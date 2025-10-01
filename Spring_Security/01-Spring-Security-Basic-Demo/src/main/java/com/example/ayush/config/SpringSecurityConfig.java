package com.example.ayush.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * Defines a password encoder bean. It's crucial to always encode passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the user details service with in-memory users.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        // The InMemoryUserDetailsManager holds the user details in memory
        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * Configures the security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for REST API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll() // Allow all requests to "/"
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/offers").authenticated() // Require authentication for "/offers"
                        .requestMatchers("/balance").hasAnyRole("USER", "ADMIN") // Require "USER" role for "/balance"
                        .requestMatchers("/approveLoan").hasRole("ADMIN") // Require "ADMIN" role for "/approveLoan"
                        .anyRequest().authenticated())
                .logout(Customizer.withDefaults()) // Enable default logout handling
                .exceptionHandling(exception -> exception.accessDeniedPage("/denied")) // Custom access denied page
                .formLogin(Customizer.withDefaults()) // Enable form-based login
                .rememberMe(Customizer.withDefaults()) // Enable remember-me functionality
                .sessionManagement(s -> s
                        .maximumSessions(1) // Limit the maximum number of simultaneous sessions for a single user to 1
                        // When the maximum session limit is reached, prevent the new login attempt (true)
                        // If set to false, the oldest session would be invalidated instead.
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/login") // Redirect to this URL when the session expires

                )



                .build();
    }

}
