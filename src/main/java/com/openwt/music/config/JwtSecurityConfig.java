package com.openwt.music.config;

import com.openwt.music.component.JwtRequestFilter;
import com.openwt.music.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class JwtSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration
            ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests -> requests
                    .requestMatchers("/sign-up", "/songs", "/songs/{id}", "/artists", "/login", "/test").permitAll()
                    .requestMatchers("/tschuessli").hasAnyRole(UserRole.ADMIN.label, UserRole.ARTIST.label)
                    .requestMatchers("/admins", "/admins/{id}").hasRole(UserRole.ADMIN.label)
                    .requestMatchers(HttpMethod.DELETE).hasRole(UserRole.ADMIN.label)
                    .anyRequest().denyAll()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(
                    logout -> logout
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .clearAuthentication(true)
                            .logoutSuccessUrl("/tschuessli")
            );

        return http.build();
    }
}
