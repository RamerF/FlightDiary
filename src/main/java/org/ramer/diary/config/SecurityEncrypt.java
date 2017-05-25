package org.ramer.diary.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by RAMER on 5/25/2017.
 */
@Slf4j
@Component
public class SecurityEncrypt implements PasswordEncoder{
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " rawPassword : {}", rawPassword);
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.encode(rawPassword).equals(encodedPassword);
    }
}
