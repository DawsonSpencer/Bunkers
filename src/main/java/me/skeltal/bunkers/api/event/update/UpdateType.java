package me.skeltal.bunkers.api.event.update;

import me.skeltal.bunkers.util.TimeUtils;

public enum UpdateType {
    TEN_MIN(600000L),
    FIVE_MIN(300000L),
    THREE_MIN(180000L),
    TWO_MIN(120000L),
    MIN(60000L),
    TEN_SEC(10000L),
    TWENTY_SEC(20000L),
    HALF_MIN(30000L),
    FOURTY_SEC(40000L),
    FIFTY_SEC(50000L),
    FIVE_SEC(5000L),
    FOUR_SEC(2000L),
    THREE_SEC(2000L),
    TWO_SEC(2000L),
    SEC(1000L),
    HALF_SEC(500L),
    QUARTER_SEC(250L),
    TWO_TICK(100L),
    TICK(49L);

    private long last = System.currentTimeMillis();
    private long time;

    UpdateType(long time) {
        this.time = time;
    }

    public boolean elapsed() {
        if (TimeUtils.elapsed(this.last, this.time)) {
            this.last = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    public long getTime() {
        return this.time;
    }
}
