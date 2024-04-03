package com.nagarro.peertopeerapplication.configuration;

import com.nagarro.peertopeerapplication.auth.JwtAuthenticationFilter;
import com.nagarro.peertopeerapplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import static com.nagarro.peertopeerapplication.enums.Permission.*;
import static com.nagarro.peertopeerapplication.enums.Role.ADMIN;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableMethodSecurity
public class SecurityConfiguration  {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;


    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/management/**").hasAuthority(ADMIN_READ.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/management/**").hasAuthority(ADMIN_CREATE.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/management/**").hasAuthority(ADMIN_DELETE.name())
                                .requestMatchers("/api/v1/users/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyAuthority(USER_READ.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasAnyAuthority(USER_CREATE.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyAuthority(USER_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }


}
