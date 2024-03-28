package com.nagarro.peertopeerapplication.configuration;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.nagarro.peertopeerapplication.enums.Permission.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration  {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .requestMatchers("/api/v1/auth/**").hasRole("USER")
                                         .requestMatchers("/api/v1/management/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/api/v1/management/**").hasAuthority(ADMIN_READ.name())
                                        .requestMatchers(HttpMethod.POST, "/api/v1/management/**").hasAuthority(ADMIN_CREATE.name())
                                        .requestMatchers(HttpMethod.DELETE, "/api/v1/management/**").hasAuthority(ADMIN_DELETE.name())
                                        .requestMatchers("/api/v1/**").hasRole("USER")
                                        .requestMatchers(HttpMethod.GET, "/api/v1/**").hasAnyAuthority(USER_READ.name())
                                        .requestMatchers(HttpMethod.POST, "/api/v1/**").hasAnyAuthority(USER_CREATE.name())
                                        .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasAnyAuthority(USER_DELETE.name())
                                         .anyRequest().authenticated()
        )
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logout) ->
                        logout.deleteCookies("remove")
                                .invalidateHttpSession(false)
                                .logoutUrl("/api/v1/auth/logout")
                                .logoutSuccessUrl("/logout-success")
                                .addLogoutHandler(logoutHandler)

                );
        return http.build();

    }

}
