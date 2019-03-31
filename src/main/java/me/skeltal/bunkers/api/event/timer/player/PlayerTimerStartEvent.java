package me.skeltal.bunkers.api.event.timer.player;

import lombok.Getter;
import me.skeltal.bunkers.api.event.BunkersEvent;
import me.skeltal.bunkers.timer.type.PlayerTimer;

@Getter
public class PlayerTimerStartEvent extends BunkersEvent {
    private PlayerTimer playerTimer;

    public PlayerTimerStartEvent(PlayerTimer playerTimer){
        this.playerTimer = playerTimer;
    }
}
