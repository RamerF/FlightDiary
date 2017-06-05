package org.ramer.diary.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

/**
 * Created by RAMER on 6/5/2017.
 */
@Slf4j
public class IntegerUtilTest{
    @Test(expected = NullPointerException.class)
    public void testInteger() throws Exception {
        Integer i1 = new Integer(2);
        Integer i2 = null;
        Integer i3 = new Integer(-2);
        Integer i4 = new Integer(0);
        Assert.assertThat(true, equalTo(IntegerUtil.greaterThanZero(i1)));
        Assert.assertThat(false, equalTo(IntegerUtil.greaterThanZero(i2)));
        Assert.assertThat(false, equalTo(IntegerUtil.greaterThanZero(i3)));
        Assert.assertThat(false, equalTo(IntegerUtil.greaterThanZero(i4)));
        log.debug("null,{}", i1.compareTo(i2));
    }
}
