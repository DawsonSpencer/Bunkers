package me.skeltal.bunkers.api.event.timer.player;

import lombok.Getter;
import me.skeltal.bunkers.api.event.BunkersEvent;
import me.skeltal.bunkers.timer.type.PlayerTimer;
import org.bukkit.entity.Player;

@Getter
public class PlayerTimerExpireEvent extends BunkersEvent {
    private Player player;
    private PlayerTimer playerTimer;

    public PlayerTimerExpireEvent(Player player, PlayerTimer playerTimer){
        this.player = player;
        this.playerTimer = playerTimer;
    }
}
