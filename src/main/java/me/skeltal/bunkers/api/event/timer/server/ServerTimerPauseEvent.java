package me.skeltal.bunkers.api.event.timer.server;

import lombok.Getter;
import me.skeltal.bunkers.api.event.BunkersEvent;
import me.skeltal.bunkers.timer.type.ServerTimer;

@Getter
public class ServerTimerPauseEvent extends BunkersEvent {
    private ServerTimer serverTimer;

    public ServerTimerPauseEvent(ServerTimer serverTimer){
        this.serverTimer = serverTimer;
    }
}
