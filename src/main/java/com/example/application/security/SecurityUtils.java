package com.example.application.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final SecurityService securityService;

    public SecurityUtils(SecurityService securityService) {
        this.securityService = securityService;
    }

    public boolean isUserLoggedIn() {
        return securityService.getAuthenticatedUser() != null;
    }
}