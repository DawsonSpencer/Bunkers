package me.skeltal.bunkers.api.event.timer.server;

import lombok.Getter;
import me.skeltal.bunkers.api.event.BunkersEvent;
import me.skeltal.bunkers.timer.type.ServerTimer;

@Getter
public class ServerTimerExpireEvent extends BunkersEvent {
    private ServerTimer serverTimer;

    public ServerTimerExpireEvent(ServerTimer serverTimer){
        this.serverTimer = serverTimer;
    }
}
