package me.skeltal.bunkers.util;

import org.apache.commons.lang.math.NumberUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {
    private static final Pattern TIME_PARSE_PATTERN = Pattern.compile("([0-9]+)([smhdwMy]{1})");
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static String millisToRoundedTime(long millis) {
        ++millis;
        long seconds = millis / 1000L;
        long minutes = seconds / 60L;
        long hours = minutes / 60L;
        long days = hours / 24L;
        long weeks = days / 7L;
        long months = weeks / 4L;
        long years = months / 12L;
        if (years > 0L) {
            return years + " year" + (years == 1L ? "" : "s");
        } else if (months > 0L) {
            return months + " month" + (months == 1L ? "" : "s");
        } else if (weeks > 0L) {
            return weeks + " week" + (weeks == 1L ? "" : "s");
        } else if (days > 0L) {
            return days + " day" + (days == 1L ? "" : "s");
        } else if (hours > 0L) {
            return hours + " hour" + (hours == 1L ? "" : "s");
        } else {
            return minutes > 0L ? minutes + " minute" + (minutes == 1L ? "" : "s") : seconds + " second" + (seconds == 1L ? "" : "s");
        }
    }

    public static long getTimeFromString(String timeString) {
        long time = 0L;
        if (timeString != null && !timeString.isEmpty()) {
            Matcher matcher = TIME_PARSE_PATTERN.matcher(timeString);

            while(true) {
                label36:
                while(matcher.find()) {
                    int count = NumberUtils.toInt(matcher.group(1), 0);
                    char unit = matcher.group(2).charAt(0);
                    int m;
                    switch(unit) {
                        case 'M':
                            m = 0;

                            while(true) {
                                if (m >= count) {
                                    continue label36;
                                }

                                time += TimeUnit.DAYS.toMillis((long)YearMonth.now().plusMonths((long)m).lengthOfMonth());
                                ++m;
                            }
                        case 'd':
                            time += TimeUnit.DAYS.toMillis((long)count);
                            break;
                        case 'h':
                            time += TimeUnit.HOURS.toMillis((long)count);
                            break;
                        case 'm':
                            time += TimeUnit.MINUTES.toMillis((long)count);
                            break;
                        case 's':
                            time += TimeUnit.SECONDS.toMillis((long)count);
                            break;
                        case 'w':
                            time += TimeUnit.DAYS.toMillis((long)(7 * count));
                            break;
                        case 'y':
                            for(m = 0; m < count; ++m) {
                                time += TimeUnit.DAYS.toMillis((long)Year.now().plusYears((long)m).length());
                            }
                    }
                }

                return time;
            }
        } else {
            return time;
        }
    }

    public static String convertTime(int totalSecs) {
        int minutes = totalSecs % 3600 / 60;
        int seconds = totalSecs % 60;
        return minutes + "m" + seconds + "s";
    }

    public static String convertPlaytime(int minutes) {
        int hours = minutes / 60;
        int min = minutes % 60;
        return hours + "h" + min + "m";
    }

    public static String convert(int totalSecs) {
        int minutes = totalSecs % 3600 / 60;
        int seconds = totalSecs % 60;
        return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
    }

    public static String convert(long totalMillieSecs) {
        int totalSecs = Long.valueOf(totalMillieSecs / 1000L).intValue();
        int minutes = totalSecs % 3600 / 60;
        int seconds = totalSecs % 60;
        return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
    }

    public static long now() {
        return nowMillis() / 1000L;
    }

    public static long nowMillis() {
        return System.currentTimeMillis();
    }

    public static String formatTime(long time) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        double secs = (double)(time / 1000L);
        double mins = secs / 60.0D;
        double hours = mins / 60.0D;
        double days = hours / 24.0D;
        if (mins < 1.0D) {
            return decimalFormat.format(secs) + " Seconds";
        } else if (hours < 1.0D) {
            return decimalFormat.format(mins % 60.0D) + " Minutes";
        } else {
            return days < 1.0D ? decimalFormat.format(hours % 24.0D) + " Hours" : decimalFormat.format(days) + " Days";
        }
    }

    public static String formatTs(String ts) {
        long timestamp = Long.parseLong(ts) * 1000L;
        return (new SimpleDateFormat("yyyy.MM.dd HH.mm.ss")).format(timestamp);
    }

    public static boolean isSameDay(long ts1, long ts2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(new Date(ts1));
        cal2.setTime(new Date(ts2));
        return cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
    }
}
