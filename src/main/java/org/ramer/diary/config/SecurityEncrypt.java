package org.ramer.diary.config;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.util.EncryptUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by RAMER on 5/25/2017.
 */
@Slf4j
@Component
public class SecurityEncrypt implements AuthenticationProvider{
    @Resource
    private UserDetailsService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Object credentials = authentication.getCredentials();
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " username : {}", username);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " passwords : {}", credentials);
        UserDetails user = userService.loadUserByUsername(username);
        if (!EncryptUtil.matches(credentials.toString(), user.getPassword())) {
            throw new BadCredentialsException("密码不对头");
        }
        return new UsernamePasswordAuthenticationToken(user, credentials, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
