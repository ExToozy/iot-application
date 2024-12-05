package ru.extoozy.iotapplication.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.extoozy.iotapplication.api.dto.ErrorDto;
import ru.extoozy.iotapplication.api.security.jwt.JwtTokenFilter;
import ru.extoozy.iotapplication.api.security.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper jsonMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedHandler(
                                        (request, response, accessDeniedException) -> {
                                            response.setStatus(HttpStatus.FORBIDDEN.value());
                                            response.getWriter().write(
                                                    jsonMapper.writeValueAsString(
                                                            ErrorDto
                                                                    .builder()
                                                                    .message("Forbidden")
                                                                    .build()
                                                    )
                                            );
                                        }
                                )
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            authException.printStackTrace();
                                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                            response.getWriter().write(
                                                    jsonMapper.writeValueAsString(
                                                            ErrorDto
                                                                    .builder()
                                                                    .message("Unauthorized")
                                                                    .build()
                                                    )
                                            );
                                        }
                                )

                )
                .authorizeHttpRequests(matcherRegistry ->
                        matcherRegistry
                                .requestMatchers("/api/v*/auth/**", "/api-docs", "/swagger-ui.html").permitAll()
                                .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        new JwtTokenFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }
}
