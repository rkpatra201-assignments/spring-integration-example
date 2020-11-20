package com.spring.springintegrationexample.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverterUtils {

    public static String getFormattedDate(String dateInputStr) throws ParseException {
        String date = dateInputStr;
        Date dateFromStr = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").parse(date);
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(dateFromStr);
        return formattedDate;
    }
}
