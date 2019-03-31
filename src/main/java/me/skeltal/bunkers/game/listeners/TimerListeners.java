package me.skeltal.bunkers.game.listeners;

import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.api.event.timer.server.ServerTimerExpireEvent;
import me.skeltal.bunkers.game.Game;
import me.skeltal.bunkers.timer.type.ServerTimer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TimerListeners implements Listener {

    @EventHandler
    public void onServerTimerExpire(ServerTimerExpireEvent event) {
        Game game = Bunkers.getInstance().getGame();
        ServerTimer timer = event.getServerTimer();
        switch (timer.getServerTimerType()) {
            case COUNTDOWN: {
                game.start();
                break;
            }
        }
    }
}
