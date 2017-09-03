/*
 *
 */
package org.ramer.diary.util;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.config.SystemConstant;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.security.SecureRandom;

/**
 * 加密类
 * @author ramer
 */
@Slf4j
@Component
public class EncryptUtil{
    @Resource
    private SystemConstant systemConstant;
    private static BCryptPasswordEncoder encoder;

    @PostConstruct
    public void initBCryptPasswordEncoder() {
        encoder = new BCryptPasswordEncoder(systemConstant.getEncryptStrength(), new SecureRandom("ramer".getBytes()));
    }

    public static String execEncrypt(Object string) {
        log.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "  : [{}]", encoder);
        String encode = encoder.encode(string.toString());
        log.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "  加密: [{}],[{}]", string, encode);
        return encode;
    }

    public static boolean matches(String plainPassword, String encodedPassword) {
        boolean matches = encoder.matches(plainPassword, encodedPassword);
        log.info(Thread.currentThread().getStackTrace()[1].getMethodName() + "  密码匹配: [{}]", matches);
        return matches;
    }
}
