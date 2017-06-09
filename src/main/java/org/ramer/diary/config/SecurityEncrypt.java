package org.ramer.diary.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Created by RAMER on 5/25/2017.
 */
@Slf4j
@Component
public class SecurityEncrypt implements AuthenticationProvider{
    @Autowired
    private UserDetailsService userService;
    @Value("${diary.encrypt.strength}")
    private int ENCRYPT_STRENGTH;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Object credentials = authentication.getCredentials();
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " username : {}", username);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " passwords : {}", credentials);
        UserDetails user = userService.loadUserByUsername(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(ENCRYPT_STRENGTH,
                new SecureRandom(username.getBytes()));
        credentials = encoder.encode((String) credentials);
        return new UsernamePasswordAuthenticationToken(user, credentials, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
