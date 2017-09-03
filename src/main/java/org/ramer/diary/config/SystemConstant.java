package org.ramer.diary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Created by RAMER on 9/3/2017.
 */
@Component
@Data
public class SystemConstant{
    @Value("${diary.encrypt.strength}")
    private int encryptStrength;
}
