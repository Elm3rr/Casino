package com.Proyecto.Final.Seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**", "/auditoria/**").hasRole("ADMIN")
                .requestMatchers( "/robot/**", "/combate/**", "/mod/**", "/data/**").hasAnyRole("ADMIN", "MOD")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "MOD")
                .requestMatchers("/transaccion/**").hasRole("USER")
                .requestMatchers("**", "/images/**", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout.permitAll()
            .logoutSuccessUrl("/login")
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .expiredSessionStrategy(request -> {
                    request.getResponse().sendRedirect("/login");
                })

            )
            .exceptionHandling(exception-> exception
            .accessDeniedPage("/error/403")
            );

        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
