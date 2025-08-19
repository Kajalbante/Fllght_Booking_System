//package com.search.security;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints
//            .authorizeHttpRequests(auth -> auth
//                // Public endpoints
//                .requestMatchers(
//                    "/api/auth/register",
//                    "/api/auth/login"
//                ).permitAll()
//                
//                // Flight endpoints require authentication
//                .requestMatchers(
//                    "/api/flights/**",
//                    "/api/flights/search"
//                ).authenticated()
//                
//                // All other requests require authentication
//                .anyRequest().authenticated()
//            )
//            .httpBasic(withDefaults()); // Enable Basic Authentication
//        
//        return http.build();
//    }
//}
package com.search.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        
        return http.build();
    }
//    no need to add passwordEncoder in this class like auth-service 
}
  