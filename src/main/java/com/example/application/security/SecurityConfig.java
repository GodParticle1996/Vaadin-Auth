package com.example.application.security;

import com.example.application.views.list.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private static final String LOGIN_URL = "/login";
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Allow access to specific paths that aren't covered by VaadinWebSecurity defaults
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
        );

        // Configure Vaadin-specific security settings (CSRF, internal requests, static resources, etc.)
        super.configure(http);

        // Configure the login view
        setLoginView(http, LoginView.class);

        // Configure login and logout
        http.formLogin(form -> form
                                .loginPage(LOGIN_URL).permitAll()
                                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                                .failureUrl(LOGIN_FAILURE_URL)
                        // Remove defaultSuccessUrl to let Vaadin handle the redirect
                )
                .logout(logout -> logout
                        .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                );
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        return new SimpleInMemoryUserDetailsManager();
    }

    private static class SimpleInMemoryUserDetailsManager extends InMemoryUserDetailsManager {
        public SimpleInMemoryUserDetailsManager() {
            createUser(new User("user", "{noop}userpass", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))));
        }
    }
}