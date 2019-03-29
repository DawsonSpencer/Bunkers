package me.skeltal.bunkers.util.scoreboard.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class BoardDestroyEvent extends Event implements Cancellable {

    @Getter
    public static HandlerList handlerList = new HandlerList();

    private Player player;
    private boolean cancelled = false;

    public BoardDestroyEvent(Player player) {
        this.player = player;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    
    public Player getPlayer() {
		return player;
	}
    
    public boolean isCancelled() {
		return cancelled;
	}
}
