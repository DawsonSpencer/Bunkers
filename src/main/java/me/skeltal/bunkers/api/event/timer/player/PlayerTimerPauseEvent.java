package me.skeltal.bunkers.api.event.timer.player;

import lombok.Getter;
import me.skeltal.bunkers.api.event.BunkersEvent;
import me.skeltal.bunkers.timer.type.PlayerTimer;

@Getter
public class PlayerTimerPauseEvent extends BunkersEvent {
    private PlayerTimer playerTimer;

    public PlayerTimerPauseEvent(PlayerTimer playerTimer){
        this.playerTimer = playerTimer;
    }
}
