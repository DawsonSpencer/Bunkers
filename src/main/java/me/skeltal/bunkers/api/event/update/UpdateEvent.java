package me.skeltal.bunkers.api.event.update;

import lombok.Getter;
import me.skeltal.bunkers.api.event.BunkersEvent;

@Getter
public class UpdateEvent extends BunkersEvent {
    private UpdateType type;

    public UpdateEvent(UpdateType type) {
        this.type = type;
    }
}
