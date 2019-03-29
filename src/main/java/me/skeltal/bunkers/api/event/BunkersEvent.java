package me.skeltal.bunkers.api.event;

import me.skeltal.bunkers.Bunkers;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BunkersEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public void call() {
        if (this instanceof Cancellable && ((Cancellable)this).isCancelled()) return;
        Bunkers.getInstance().getServer().getPluginManager().callEvent(this);
    }
}
