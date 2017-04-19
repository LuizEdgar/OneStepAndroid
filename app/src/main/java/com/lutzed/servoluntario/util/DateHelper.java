package com.lutzed.servoluntario.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateHelper {

    public static final DateFormat iso8601Format = buildIso8601Format();
    public static final DateFormat isoDayFormat = buildIsoDayFormat();
    public static final DateFormat postDatetimeFormat = buildPostDatetimeFormat();
    public static final DateFormat eventDatetimeFormat = buildEventDatetimeFormat();
    public static final DateFormat dateFormat = buildDateFormat();
    public static final DateFormat timeFormat = buildTimeFormat();
    public static final DateFormat dayFormat = buildDayFormat();

    public static final DateFormat monthFormat = buildMonthFormat();
    private static final long SECOND_MILI = 1000;

    private static final long MINUTE_MILI = SECOND_MILI * 60;
    private static final long HOUR_MILI = MINUTE_MILI * 60;
    private static final long DAY_MILI = HOUR_MILI * 24;
    private static final long WEEK_MILI = DAY_MILI * 7;
    private static final long YEAR_MILI = DAY_MILI * 365;
    private static DateFormat buildIso8601Format() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    private static DateFormat buildIsoDayFormat() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }

    private static DateFormat buildPostDatetimeFormat() {
        DateFormat dayFormat = null;

        dayFormat = new SimpleDateFormat("dd MMM - HH:mm", Locale.getDefault());
        return dayFormat;
    }

    private static DateFormat buildDateFormat() {
        DateFormat dayFormat = null;

        dayFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        return dayFormat;
    }

    private static DateFormat buildTimeFormat() {
        DateFormat dayFormat = null;

        dayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dayFormat;
    }

    private static DateFormat buildEventDatetimeFormat() {
        DateFormat dayFormat = null;

        dayFormat = new SimpleDateFormat("dd MMMM - HH:mm", Locale.getDefault());
        return dayFormat;
    }

    private static DateFormat buildDayFormat() {
        return new SimpleDateFormat("dd", Locale.getDefault());
    }

    private static DateFormat buildMonthFormat() {
        return new SimpleDateFormat("MMM", Locale.getDefault());
    }

    public static String format(DateFormat formatOut, String dateString) {
        if (dateString == null) return null;

        return format(formatOut, deserialize(dateString));
    }

    public static String format(DateFormat formatOut, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        return format(formatOut, c.getTime());
    }

    public static String format(DateFormat formatOut, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        return format(formatOut, c.getTime());
    }


    public static String format(DateFormat formatOut, Date date) {
        if (date == null) return null;

        return (formatOut.format(date));
    }

    public static Date deserialize(String dateString) throws NullPointerException {
        try {
            return iso8601Format.parse(dateString);
        } catch (ParseException ignored) {
//            ignored.printStackTrace();
        }

        try {
            return isoDayFormat.parse(dateString);
        } catch (ParseException ignored) {
//            ignored.printStackTrace();
        }

        return null;
    }

    public static String getFuzzyTime(String date, boolean completeName) {

        Calendar today = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(deserialize(date));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "?";
        }

        long milsecs2 = today.getTimeInMillis();
        long milsecs1 = cal.getTimeInMillis();

        long diff = Math.max(milsecs2, milsecs1) - Math.min(milsecs2, milsecs1);

        if (diff < 0) diff = diff * (-1);
        return tranformMillisToFuzzy(diff, completeName);
    }

    public static String tranformMillisToFuzzy(long diff, boolean completeName) {
        long dsecs = diff / SECOND_MILI;
        if (dsecs < 60) {
            return Long.toString(dsecs) + (completeName ? (dsecs > 1 ? " seconds" : " second") : "s");
        }
        long dminutes = diff / MINUTE_MILI;
        if (dminutes < 60) {
            return Long.toString(dminutes) + (completeName ? (dminutes > 1 ? " minutes" : " minute") : "m");
        }
        long dhours = diff / HOUR_MILI;
        if (dhours < 24) {
            return Long.toString(dhours) + (completeName ? (dhours > 1 ? " hours" : " hour") : "h");
        }
        long ddays = diff / DAY_MILI;
        if (ddays < 7) {
            return Long.toString(ddays) + (completeName ? (ddays > 1 ? " days" : " day") : "d");
        }
        long dweeks = diff / WEEK_MILI;
        if (dweeks < 52) {
            return Long.toString(dweeks) + (completeName ? (dweeks > 1 ? " weeks" : " week") : "w");
        }
        long dyears = diff / YEAR_MILI;
        return Long.toString(dyears) + (completeName ? (dyears > 1 ? " years" : " year") : "y");
    }

    public static String tranformMillisToDuration(long millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

    }


}