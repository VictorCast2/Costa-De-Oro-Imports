package com.application.configuration.security;

import com.application.configuration.Custom.CustomAuthFailureHandler;
import com.application.configuration.Custom.CustomAuthSuccessHandler;
import com.application.configuration.Custom.CustomOauth2UserService;
import com.application.configuration.filter.RecaptchaFilter;
import com.application.service.implementation.usuario.UsuarioServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomAuthSuccessHandler customAuthSuccessHandler,
            CustomAuthFailureHandler customAuthFailureHandler,
            CustomOauth2UserService customOauth2UserService,
            RecaptchaFilter recaptchaFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/auth/login")
                        .maximumSessions(2)
                        .expiredUrl("/auth/login?expired")
                        .sessionRegistry(sessionRegistry())
                )
                .authorizeHttpRequests(auth -> auth
                        // Configurar endpoints privados
                        /* ----- Admin ----- */
                        .requestMatchers("/admin/categoria/**").hasRole("ADMIN")
                        .requestMatchers("/admin/producto/**").hasRole("ADMIN")

                        // Configurar endpoints públicos (sin autenticación)
                        // Principal Controller
                        .requestMatchers("/**").permitAll()

                        // Autenticación Controller
                        .requestMatchers(HttpMethod.GET, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                                                // Configurar endpoints públicos estáticos (sin autenticación)
                                                .requestMatchers("/", "/Assets/**", "/Js/**", "/Css/**").permitAll()
                        // Configurar endpoints NO ESPECIFICADOS
                        .anyRequest().authenticated()
                )
                .addFilterBefore(recaptchaFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .successHandler(customAuthSuccessHandler)
                        .failureHandler(customAuthFailureHandler)
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .successHandler(customAuthSuccessHandler)
                        .failureHandler(customAuthFailureHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2UserService)
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .deleteCookies("JSESSIONID", "access_token")
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error/403")
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UsuarioServiceImpl usuario) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(usuario);
        authenticationProvider.setPasswordEncoder(this.passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}