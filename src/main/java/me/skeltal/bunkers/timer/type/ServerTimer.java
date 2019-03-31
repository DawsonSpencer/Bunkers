package me.skeltal.bunkers.timer.type;

import lombok.Getter;
import lombok.Setter;
import me.skeltal.bunkers.api.event.timer.server.ServerTimerPauseEvent;
import me.skeltal.bunkers.api.event.timer.server.ServerTimerUnPauseEvent;
import me.skeltal.bunkers.util.CC;
import me.skeltal.bunkers.util.DateUtil;
import org.bson.Document;

import java.util.Calendar;
import java.util.Date;

/**
 * @author TewPingz
 */
@Getter
@Setter
public class ServerTimer {

    /* longs */
    private long started;
    private long duration;

    /* type */
    private ServerTimerType serverTimerType;

    /* pause */
    private boolean paused;
    private long pauseStarted;
    private long pauseDuration;

    /* activity */
    private boolean active;

    /**
     * @param started when the timer started.
     * @param duration the duration of the timer.
     * @param serverTimerType the type of timer.
     * @param paused if the timer is paused.
     * @param pauseStarted when the timer was paused.
     * @param pauseDuration the timers pause duration.
     * @param active if the timer is active.
     */
    public ServerTimer(long started, long duration, ServerTimerType serverTimerType, boolean paused, long pauseStarted, long pauseDuration, boolean active){
        this.started = started;
        this.duration = duration;
        this.serverTimerType = serverTimerType;
        this.paused = paused;
        this.pauseStarted = pauseStarted;
        this.pauseDuration = pauseDuration;
        this.active = active;
    }

    /**
     * @param started when the timer was started.
     * @param duration the duration of the timer.
     * @param serverTimerType the timer type.
     */
    public ServerTimer(long started, long duration, ServerTimerType serverTimerType){
        this.started = started;
        this.duration = duration;
        this.serverTimerType = serverTimerType;
        this.paused = false;
        this.pauseStarted = 0L;
        this.pauseDuration = 0L;
        this.active = true;
    }

    /**
     * @param duration the duration of the timer.
     * @param serverTimerType the type of the timer.
     */
    public ServerTimer(long duration, ServerTimerType serverTimerType){
        this.started = System.currentTimeMillis();
        this.duration = duration;
        this.serverTimerType = serverTimerType;
        this.paused = false;
        this.pauseStarted = 0L;
        this.pauseDuration = 0L;
        this.active = true;
    }

    /**
     * Called to pause the timer.
     */
    public void pause(){
        if (!this.paused){
            this.paused = true;
            this.pauseStarted = System.currentTimeMillis();
            new ServerTimerPauseEvent(this).call();
        }
    }

    /**
     * Called to unpause the timer.
     */
    public void unpause(){
        if (this.paused){
            this.paused = false;
            this.pauseDuration = pauseDuration + Math.abs(this.pauseStarted - this.pauseDuration);
            new ServerTimerUnPauseEvent(this).call();
        }
    }

    /**
     * @return if the cooldown is active or not.
     */
    public boolean hasExpired(){
        final long duration = this.pauseDuration + this.duration;

        if (this.paused){
            return !(this.pauseStarted <= (this.pauseStarted + duration));
        } else {
            return !(System.currentTimeMillis() <= (this.started + duration));
        }
    }

    /**
     * @return the amount of time left in a string.
     */
    public String getTimeLeft(){
        final long duration = this.pauseDuration + this.duration;

        if (hasExpired()) {
            return "Expired";
        }
        if (this.paused){
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.setTime(new Date(this.pauseStarted));
            to.setTime(new Date(this.pauseStarted + duration));
            return DateUtil.formatSimplifiedDateDiff(from, to);
        } else {
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.setTime(new Date(System.currentTimeMillis()));
            to.setTime(new Date(this.started + duration));
            return DateUtil.formatSimplifiedDateDiff(from, to);
        }
    }

    /**
     * @return the duration left.
     */
    public long getDuration(){
        if (this.paused) {
            return this.pauseStarted + (this.pauseDuration + this.duration);
        } else {
            return this.started + (this.pauseDuration + this.duration);
        }
    }

    /**
     * @return a document with all the timer database contained.
     */
    public Document toDocument(){
        Document document = new Document();

        document.put("started", this.started);
        document.put("duration", this.duration);
        document.put("type", this.serverTimerType.name);
        document.put("paused", this.paused);
        document.put("pauseDuration", this.pauseDuration);
        document.put("pauseStarted", this.pauseStarted);
        document.put("active", this.active);

        return document;
    }

    @Getter
    public enum ServerTimerType {
        COUNTDOWN("Countdown", CC.GOLD + CC.BOLD + "Starting In: " + CC.RESET);

        private String name;
        private String scoreboardPrefix;

        /**
         * @param name the name of the enum instance.
         * @param scoreboardPrefix the scoreboardPrefix of the enum instance.
         */
        ServerTimerType(String name, String scoreboardPrefix){
            this.name = name;
            this.scoreboardPrefix = scoreboardPrefix;
        }
    }
}
