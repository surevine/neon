package com.surevine.neon.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date util to standardise conversion between Date objects and String representations
 */
public final class DateUtil {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private DateUtil() {}
    
    public static String dateToString(final Date date) {
        return date == null ? null : DATE_FORMAT.format(date);
    }
    
    public static Date stringToDate(final String dateString) {
        Date date = null;
        try {
            date = DATE_FORMAT.parse(dateString);
        } catch (ParseException pe) {
            // drop it - if it's not a valid date string return null
        }
        return date;
    }
}
