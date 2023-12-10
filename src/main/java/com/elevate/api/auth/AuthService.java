package com.elevate.api.auth;

import com.elevate.api.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthService {
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    private final JWTGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(JWTGenerator jwtGenerator, AuthenticationManager authenticationManager) {
        this.jwtGenerator = jwtGenerator;
        this.authenticationManager = authenticationManager;
    }

    public String Authenticate(Credential credential) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtGenerator.generateToken(authentication);
        } catch (BadCredentialsException e) {
            logger.info("Auth Error: " + e.getMessage());
            throw new AuthException("Invalid username or password");
        }
    }
}
