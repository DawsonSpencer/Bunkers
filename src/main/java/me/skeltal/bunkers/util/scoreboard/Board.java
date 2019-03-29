package me.skeltal.bunkers.util.scoreboard;

import lombok.Getter;
import me.skeltal.bunkers.util.scoreboard.BoardEntry;
import me.skeltal.bunkers.util.scoreboard.PlayerBoard;
import me.skeltal.bunkers.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Board {

	// We assign a unique identifier (random string of ChatColor values)
	// to each board entry to: bypass the 32 char limit, using
	// a team's prefix & suffix and a team entry's display name, and to
	// track the order of entries;
	private final List<BoardEntry> entries = new ArrayList<>();
	private final List<String> identifiers = new ArrayList<>();
	private Scoreboard scoreboard;
	private Objective objective;

	public Board(Player player) {
		this.setup(player);
	}

	private void setup(Player player) {
		// Register new scoreboard if needed
		if (player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
			this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		} else {
			this.scoreboard = player.getScoreboard();
		}

		// Setup sidebar objective
		this.objective = this.scoreboard.registerNewObjective("Default", "dummy");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.objective.setDisplayName(PlayerBoard.getInstance().getAdapter().getTitle(player) + CC.RESET);

		// Update scoreboard
		player.setScoreboard(this.scoreboard);
	}

	public BoardEntry getEntryAtPosition(int pos) {
		if (pos >= this.entries.size()) {
			return null;
		} else {
			return this.entries.get(pos);
		}
	}

	public String getUniqueIdentifier(String text) {
		String identifier = getRandomChatColor() + ChatColor.WHITE;

		while (this.identifiers.contains(identifier)) {
			identifier = identifier + getRandomChatColor() + ChatColor.WHITE;
		}

		// This is rare, but just in case, make the method recursive
		if (identifier.length() > 16) {
			return this.getUniqueIdentifier(text);
		}

		// Add our identifier to the list so there are no duplicates
		this.identifiers.add(identifier);

		return identifier;
	}

	// Gets a random ChatColor and returns the String value of it
	private static String getRandomChatColor() {
		return ChatColor.values()[ThreadLocalRandom.current().nextInt(ChatColor.values().length)].toString();
	}

	public List<BoardEntry> getEntries() {
		return entries;
	}
	
	public List<String> getIdentifiers() {
		return identifiers;
	}
	
	public Objective getObjective() {
		return objective;
	}
	
	public Scoreboard getScoreboard() {
		return scoreboard;
	}
}
