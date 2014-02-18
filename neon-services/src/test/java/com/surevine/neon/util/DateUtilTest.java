package com.surevine.neon.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class DateUtilTest {
    @Test
    public void testRoundTripDateConversion() {
        String original = "2014-03-10 10:20:30";
        Date interim = DateUtil.stringToDate(original);
        String reparse = DateUtil.dateToString(interim);
        
        assertTrue(original.equals(reparse));
    }
}
