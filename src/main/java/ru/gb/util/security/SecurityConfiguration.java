package ru.gb.util.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.AbstractSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/ui/issues/**").hasAuthority("admin")
                        .requestMatchers("/ui/readers/**").hasAuthority("reader")
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .build();
    }
}