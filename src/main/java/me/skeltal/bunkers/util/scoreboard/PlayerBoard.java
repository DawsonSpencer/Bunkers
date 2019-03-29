package me.skeltal.bunkers.util.scoreboard;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerBoard {

	@Getter
	private static PlayerBoard instance;

	private JavaPlugin plugin;
	private BoardAdapter adapter;
	private Map<UUID, Board> boards;
	private BoardThread thread;

	@Setter
	private long ticks = 2;

	@Setter
	private BoardStyle boardStyle = BoardStyle.MODERN;

	public PlayerBoard(JavaPlugin plugin, BoardAdapter adapter) {
		if (instance != null) {
			throw new RuntimeException("Board has already been instantiated!");
		}

		if (plugin == null) {
			throw new RuntimeException("Board can not be instantiated without a plugin instance!");
		}

		instance = this;

		this.plugin = plugin;
		this.adapter = adapter;
		this.boards = new ConcurrentHashMap<>();

		this.setup();
	}

	private void setup() {
		//Register Events
		this.plugin.getServer().getPluginManager().registerEvents(new BoardListener(), this.plugin);

		//Ensure that the thread has stopped running
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		//Start Thread
		this.thread = new BoardThread(this);
	}

}
