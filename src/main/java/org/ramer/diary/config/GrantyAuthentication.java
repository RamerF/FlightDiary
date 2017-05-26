package org.ramer.diary.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by RAMER on 5/26/2017.
 */
@Component
@Slf4j
public class GrantyAuthentication implements AccessDecisionManager{

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " authentication : {}", authentication);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " object : {}", object);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " configAttributes : {}",
                configAttributes);
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " attribute : {}", attribute);
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " clazz : {}", clazz);
        return true;
    }
}
