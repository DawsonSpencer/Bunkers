package me.skeltal.bunkers.timer.type;

import lombok.Getter;
import lombok.Setter;
import me.skeltal.bunkers.api.event.timer.player.PlayerTimerPauseEvent;
import me.skeltal.bunkers.api.event.timer.player.PlayerTimerUnPauseEvent;
import me.skeltal.bunkers.util.CC;
import me.skeltal.bunkers.util.DateUtil;
import org.bson.Document;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author TewPingz
 */
@Getter
@Setter
public class PlayerTimer {

    /* Player */
    private UUID uuid;

    /* Times */
    private long started;
    private long duration;

    /* Type */
    private PlayerTimerType playerTimerType;

    /* Pause Info */
    private boolean paused;
    private long pauseStarted;
    private long pauseDuration;
    private boolean active;

    /**
     * @param uuid the uuid of the player.
     * @param started when the timer started.
     * @param duration the duration of the timer.
     * @param playerTimerType the type of timer.
     * @param paused if the timer is paused.
     * @param pauseStarted when did the timer get paused.
     * @param pauseDuration the duration of the pause.
     * @param active if the timer is active or not.
     */
    public PlayerTimer(UUID uuid, long started, long duration, PlayerTimerType playerTimerType, boolean paused, long pauseStarted, long pauseDuration, boolean active){
        this.uuid = uuid;
        this.started = started;
        this.duration = duration;
        this.playerTimerType = playerTimerType;
        this.paused = paused;
        this.pauseStarted = pauseStarted;
        this.pauseDuration = pauseDuration;
        this.active = active;
    }

    /**
     * @param uuid the players uuid.
     * @param started when the timer was started.
     * @param duration the duration of the timer.
     * @param playerTimerType the timer type.
     */
    public PlayerTimer(UUID uuid, long started, long duration, PlayerTimerType playerTimerType){
        this.uuid = uuid;
        this.started = started;
        this.duration = duration;
        this.playerTimerType = playerTimerType;
        this.paused = false;
        this.pauseStarted = 0L;
        this.pauseDuration = 0L;
        this.active = true;
    }

    /**
     * @param uuid the players uuid.
     * @param duration the duration of the timer.
     * @param playerTimerType the type of the timer.
     */
    public PlayerTimer(UUID uuid, long duration, PlayerTimerType playerTimerType){
        this.uuid = uuid;
        this.started = System.currentTimeMillis();
        this.duration = duration;
        this.playerTimerType = playerTimerType;
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
            new PlayerTimerPauseEvent(this).call();
        }
    }

    /**
     * Called to unpause the timer.
     */
    public void unpause(){
        if (this.paused){
            this.paused = false;
            this.pauseDuration = pauseDuration + Math.abs(this.pauseStarted - this.pauseDuration);
            new PlayerTimerUnPauseEvent(this).call();
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
     * @return a document with all the timers database.
     */
    public Document toDocument(){
        Document document = new Document();

        document.put("uuid", this.uuid.toString());
        document.put("started", this.started);
        document.put("duration", this.duration);
        document.put("type", this.playerTimerType.name);
        document.put("paused", this.paused);
        document.put("pauseDuration", this.pauseDuration);
        document.put("pauseStarted", this.pauseStarted);
        document.put("active", this.active);

        return document;
    }

    @Getter
    public enum PlayerTimerType {
        ENDER_PEARL("Enderpearl", CC.DARK_PURPLE + CC.BOLD + "Enderpearl: " + CC.RESET),
        HOME("Home", CC.BLUE + CC.BOLD + "Home: " + CC.RESET);

        private String name;
        private String scoreboardPrefix;

        /**
         * @param name the name of the enum instance.
         * @param scoreboardPrefix the scoreboardPrefix of the enum instance.
         */
        PlayerTimerType(String name, String scoreboardPrefix){
            this.name = name;
            this.scoreboardPrefix = scoreboardPrefix;
        }
    }
}
