package me.skeltal.bunkers.util;

import java.beans.ConstructorProperties;

public enum Duration {
    SECOND(1000L, "s"),
    MINUTE(60L * SECOND.duration, "m"),
    HOUR(60L * MINUTE.duration, "h"),
    DAY(24L * HOUR.duration, "d"),
    WEEK(7L * DAY.duration, "w"),
    MONTH(30L * DAY.duration, "M"),
    YEAR(365L * DAY.duration, "y");

    private final long duration;
    private final String name;

    public static Duration getByName(String name) {
        Duration[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Duration duration = var1[var3];
            if (duration.getName().equals(name)) {
                return duration;
            }
        }

        return null;
    }

    public long getDuration() {
        return this.duration;
    }

    public String getName() {
        return this.name;
    }

    @ConstructorProperties({"duration", "name"})
    private Duration(long duration, String name) {
        this.duration = duration;
        this.name = name;
    }
}
