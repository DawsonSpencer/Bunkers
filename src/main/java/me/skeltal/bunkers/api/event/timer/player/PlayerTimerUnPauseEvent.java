package me.skeltal.bunkers.api.event.timer.player;

import lombok.Getter;
import me.skeltal.bunkers.api.event.BunkersEvent;
import me.skeltal.bunkers.timer.type.PlayerTimer;

@Getter
public class PlayerTimerUnPauseEvent extends BunkersEvent {
    private PlayerTimer playerTimer;

    public PlayerTimerUnPauseEvent(PlayerTimer playerTimer){
        this.playerTimer = playerTimer;
    }
}
