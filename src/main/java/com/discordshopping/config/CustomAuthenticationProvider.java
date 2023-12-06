package com.discordshopping.config;

import com.discordshopping.service.UserService;
import com.discordshopping.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        boolean passwordIsValid = checker(username, password);

        if (passwordIsValid) {
            return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        } else {
            throw new AuthenticationException("Invalid login or password") {
            };
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean checker(String username, String password) {
        try {
            if (userService.findByPasswordAndEmail(Util.encode(password).toString(), username) != null) {
                return true;
            }
        } catch (Throwable ignore) {
        }
        return false;
    }
}

