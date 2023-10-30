package com.sojrel.saccoapi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    public static String convertDateFormat(String inputDate) {
        try {
            // Parse the input date string
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM", Locale.ENGLISH);
            Date date = inputDateFormat.parse(inputDate);

            // Format the date to 'Month yyyy'
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
