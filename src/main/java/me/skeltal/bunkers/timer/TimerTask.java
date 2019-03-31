package me.skeltal.bunkers.timer;

import lombok.Getter;
import lombok.Setter;
import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.api.event.timer.player.PlayerTimerExpireEvent;
import me.skeltal.bunkers.api.event.timer.server.ServerTimerExpireEvent;
import me.skeltal.bunkers.profile.GameProfile;
import me.skeltal.bunkers.timer.type.PlayerTimer;
import me.skeltal.bunkers.timer.type.ServerTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TewPingz
 */
@Getter
@Setter
public class TimerTask extends BukkitRunnable {
    private TimerManager timerManager;

    public TimerTask(TimerManager timerManager){
        this.timerManager = timerManager;
        this.runTaskTimerAsynchronously(Bunkers.getInstance(), 2L, 2L);
    }

    @Override
    public void run() {
        List<ServerTimer> serverTimersList = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            final GameProfile gameProfile = GameProfile.getByUuid(player.getUniqueId());

            List<PlayerTimer> toRemove = new ArrayList<>();

            for (PlayerTimer playerTimer : this.timerManager.getTimers(player)) {
                if (playerTimer.hasExpired() && playerTimer.isActive()) {
                    playerTimer.setActive(false);
                    toRemove.add(playerTimer);
                    new PlayerTimerExpireEvent(player, playerTimer).call();
                }
            }

            gameProfile.getTimers().removeAll(toRemove);
        }
        for (ServerTimer serverTimer : this.timerManager.getServerTimers()){
            if (serverTimer.hasExpired() && serverTimer.isActive()){
                serverTimer.setActive(false);
                serverTimersList.add(serverTimer);
                new ServerTimerExpireEvent(serverTimer).call();
            }
        }

        this.timerManager.getServerTimers().removeAll(serverTimersList);
    }
}
