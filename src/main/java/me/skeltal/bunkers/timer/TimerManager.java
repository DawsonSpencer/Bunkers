package me.skeltal.bunkers.timer;

import lombok.Getter;
import lombok.Setter;
import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.api.event.timer.player.PlayerTimerStartEvent;
import me.skeltal.bunkers.api.event.timer.player.PlayerTimerStopEvent;
import me.skeltal.bunkers.api.event.timer.server.ServerTimerStartEvent;
import me.skeltal.bunkers.api.event.timer.server.ServerTimerStopEvent;
import me.skeltal.bunkers.profile.GameProfile;
import me.skeltal.bunkers.timer.type.PlayerTimer;
import me.skeltal.bunkers.timer.type.ServerTimer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author TewPingz
 */
@Getter
@Setter
public class TimerManager {

    /* instance */
    private Bunkers instance;

    /* timers */
    private List<ServerTimer> serverTimers;

    /* tasks */
    private TimerTask timerTask;

    /**
     * @param plugin the instance of the main class.
     */
    public TimerManager(Bunkers plugin){
        this.instance = plugin;
        this.serverTimers = new ArrayList<>();
        this.timerTask = new TimerTask(this);
    }

    /**
     * @param player the player to add the timer to.
     * @param playerTimerType the type of timer.
     * @param duration the duration of the timer.
     */
    public void addTimer(Player player, PlayerTimer.PlayerTimerType playerTimerType, long duration){
        this.addTimer(player.getUniqueId(), playerTimerType, duration);
    }

    /**
     * @param uuid the uuid of the player.
     * @param playerTimerType the type of timer.
     * @param duration the duration of the timer.
     */
    public void addTimer(UUID uuid, PlayerTimer.PlayerTimerType playerTimerType, long duration){
        final PlayerTimer playerTimer = new PlayerTimer(uuid, duration, playerTimerType);

        if (this.hasTimer(uuid, playerTimerType)){
            this.removeTimer(uuid, playerTimerType);
        }

        GameProfile.getByUuid(uuid).getTimers().add(playerTimer);
        new PlayerTimerStartEvent(playerTimer).call();
    }

    /**
     * @param serverTimerType the type of timer.
     * @param duration the duration of the timer.
     */
    public void addTimer(ServerTimer.ServerTimerType serverTimerType, long duration){
        final ServerTimer serverTimer = new ServerTimer(duration, serverTimerType);

        if (this.hasTimer(serverTimerType)){
            this.removeTimer(serverTimerType);
        }

        this.serverTimers.add(serverTimer);
        new ServerTimerStartEvent(serverTimer).call();
    }

    /**
     * @param player the player to remove timer from.
     * @param playerTimerType the type of timer.
     */
    public void removeTimer(Player player, PlayerTimer.PlayerTimerType playerTimerType){
        this.removeTimer(player.getUniqueId(), playerTimerType);
    }

    /**
     * @param uuid the uuid of the player.
     * @param playerTimerType the type of timer.
     */
    public void removeTimer(UUID uuid, PlayerTimer.PlayerTimerType playerTimerType){
        if (this.hasTimer(uuid, playerTimerType)){
            final PlayerTimer playerTimer = this.getTimer(uuid, playerTimerType);
            GameProfile.getByUuid(uuid).getTimers().remove(playerTimer);
            new PlayerTimerStopEvent(playerTimer).call();
        }
    }

    /**
     * @param serverTimerType the type of timer.
     */
    public void removeTimer(ServerTimer.ServerTimerType serverTimerType){
        if (this.hasTimer(serverTimerType)){
            final ServerTimer serverTimer = this.getTimer(serverTimerType);
            this.serverTimers.remove(serverTimer);
            new ServerTimerStopEvent(serverTimer).call();
        }
    }

    /**
     * @param player the player to check.
     * @param playerTimerType the type of timer.
     * @return if the player has the timer.
     */
    public boolean hasTimer(Player player, PlayerTimer.PlayerTimerType playerTimerType){
        return this.hasTimer(player.getUniqueId(), playerTimerType);
    }

    /**
     * @param uuid the uuid of the player.
     * @param playerTimerType the type of timer.
     * @return if the player has the timer.
     */
    public boolean hasTimer(UUID uuid, PlayerTimer.PlayerTimerType playerTimerType){
        for (PlayerTimer playerTimer : this.getTimers(uuid)){
            if (playerTimerType == playerTimer.getPlayerTimerType()){
                return true;
            }
        }
        return false;
    }

    /**
     * @param serverTimerType the type of timer.
     * @return if the server has that timer.
     */
    public boolean hasTimer(ServerTimer.ServerTimerType serverTimerType){
        for (ServerTimer serverTimer : this.serverTimers){
            if (serverTimerType == serverTimer.getServerTimerType()){
                return true;
            }
        }
        return false;
    }

    /**
     * @param player the player to check.
     * @param playerTimerType the type of timer.
     * @return the timer instance.
     */
    public PlayerTimer getTimer(Player player, PlayerTimer.PlayerTimerType playerTimerType){
        return this.getTimer(player.getUniqueId(), playerTimerType);
    }

    /**
     * @param uuid the uuid of the player.
     * @param playerTimerType the type of timer.
     * @return the timer instance.
     */
    public PlayerTimer getTimer(UUID uuid, PlayerTimer.PlayerTimerType playerTimerType) {
        for (PlayerTimer playerTimer : this.getTimers(uuid)) {
            if (playerTimerType == playerTimer.getPlayerTimerType()) {
                return playerTimer;
            }
        }
        return null;
    }

    /**
     * @param serverTimerType the type of timer.
     * @return the timer instance.
     */
    public ServerTimer getTimer(ServerTimer.ServerTimerType serverTimerType){
        for (ServerTimer serverTimer : this.serverTimers){
            if (serverTimerType == serverTimer.getServerTimerType()){
                return serverTimer;
            }
        }
        return null;
    }

    /**
     * @param player the player instance.
     * @return the timers the player has.
     */
    public List<PlayerTimer> getTimers(Player player){
        return this.getTimers(player.getUniqueId());
    }

    /**
     * @param uuid the uuid of the player.
     * @return the timers the player has.
     */
    public List<PlayerTimer> getTimers(UUID uuid){
        return new ArrayList<>(GameProfile.getByUuid(uuid).getTimers());
    }
}
