package org.ramer.diary.util;

/**
 * Created by RAMER on 6/5/2017.
 */
public class IntegerUtil{
    private static final Integer ZERO = 0;

    /**
     *  Check whether the given {@code integer} greater than zero.
     *
     * @param integer the integer
     * @return the boolean
     */
    public static boolean greaterThanZero(Integer integer) {
        if (integer == null) {
            return false;
        }
        return integer.compareTo(ZERO) > ZERO;
    }
}
