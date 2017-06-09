/*
 *
 */
package org.ramer.diary.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密类
 * @author ramer
 */
@Slf4j
public class EncryptUtil{
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String execEncrypt(String string) {
        return encoder.encode(string);
    }
}
