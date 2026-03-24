package br.com.fiap.techchallenge.orderservice.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/restaurants/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/restaurants/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/restaurants/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/restaurants/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/menu-items/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/menu-items/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/menu-items/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/menu-items/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/restaurant/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/status").hasAnyRole("RESTAURANT_OWNER", "ADMIN")

                        .requestMatchers("/api/customers/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
