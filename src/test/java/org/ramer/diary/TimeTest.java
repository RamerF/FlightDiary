package org.ramer.diary;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 时间测试类
 * @author ramer
 *
 */
@Slf4j
public class TimeTest{

    /**
     * 测试时间
     */
    @Test
    public void testTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());
        log.debug(date);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        String date2 = simpleDateFormat.format(calendar.getTime());
        log.debug(date2);
        log.debug("{}", date.compareTo(date2));
    }

}
