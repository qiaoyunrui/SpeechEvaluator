package com.nuc.speechevaluator.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getFormatDate(Date date) {
        if (date == null) return "";
        return formatter.format(date);
    }

}
