package me.skeltal.bunkers.util.scoreboard;

import me.skeltal.bunkers.util.scoreboard.events.BoardCreateEvent;
import me.skeltal.bunkers.util.scoreboard.events.BoardDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class BoardListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		BoardCreateEvent createEvent = new BoardCreateEvent(event.getPlayer());

		Bukkit.getPluginManager().callEvent(createEvent);
		if (createEvent.isCancelled()) {
			return;
		}

		PlayerBoard.getInstance().getBoards().put(event.getPlayer().getUniqueId(), new Board(event.getPlayer()));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		BoardDestroyEvent destroyEvent = new BoardDestroyEvent(event.getPlayer());

		Bukkit.getPluginManager().callEvent(destroyEvent);
		if (destroyEvent.isCancelled()) {
			return;
		}

		PlayerBoard.getInstance().getBoards().remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		PlayerBoard.getInstance().getThread().stop();
	}

}
