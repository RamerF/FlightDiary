package org.ramer.diary;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * 加密类的测试类
 * @author ramer
 *
 */
@Slf4j
public class EncryptTest{

    /**
     * 测试加密方法.
     */
    @Test
    public void testEncrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10, new SecureRandom("feng".getBytes()));
        log.debug(encoder.encode("ramer"));
    }

}
