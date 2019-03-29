package me.skeltal.bunkers.util.scoreboard;

import me.skeltal.bunkers.util.scoreboard.Board;
import me.skeltal.bunkers.util.scoreboard.BoardEntry;
import me.skeltal.bunkers.util.scoreboard.PlayerBoard;
import me.skeltal.bunkers.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;

public class BoardThread extends Thread {

    private PlayerBoard playerBoard;

    BoardThread(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
        this.start();
    }

    @Override
    public void run() {
        while(true) {
            //Tick
            try {
                tick();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            //Thread Sleep
            try {
                sleep(playerBoard.getTicks() * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tick() {
        for (Player player : this.playerBoard.getPlugin().getServer().getOnlinePlayers()) {
            final Board board = this.playerBoard.getBoards().get(player.getUniqueId());

            // This shouldn't happen, but just in case
            if (board == null) {
                continue;
            }

            final Scoreboard scoreboard = board.getScoreboard();
            final Objective objective = board.getObjective();

            // Just make a variable so we don't have to
            // process the same thing twice
            final String title = ChatColor.translateAlternateColorCodes('&', this.playerBoard.getAdapter().getTitle(player));

            // Update the title if needed
            if (!objective.getDisplayName().equals(title)) {
                objective.setDisplayName(title + CC.RESET);
            }

            final List<String> newLines = this.playerBoard.getAdapter().getLines(player);

            // Allow adapter to return null/empty list to display nothing
            if (newLines == null || newLines.isEmpty()) {
                board.getEntries().forEach(BoardEntry::remove);
                board.getEntries().clear();
            } else {
                // Reverse the lines because scoreboard scores are in descending order
                if (!this.playerBoard.getBoardStyle().isDecending()) {
                    Collections.reverse(newLines);
                }

                // Remove excessive amount of board entries
                if (board.getEntries().size() > newLines.size()) {
                    for (int i = newLines.size(); i < board.getEntries().size(); i++) {
                        final BoardEntry entry = board.getEntryAtPosition(i);

                        if (entry != null) {
                            entry.remove();
                        }
                    }
                }

                // Update existing entries / add new entries
                int cache = this.playerBoard.getBoardStyle().getStartNumber();
                for (int i = 0; i < newLines.size(); i++) {
                    BoardEntry entry = board.getEntryAtPosition(i);

                    // Translate any colors
                    final String line = ChatColor.translateAlternateColorCodes('&', newLines.get(i));

                    // If the entry is null, just create a new one.
                    // Creating a new AssembleBoardEntry instance will add
                    // itself to the provided board's entries list.
                    if (entry == null) {
                        entry = new BoardEntry(board, line);
                    }

                    // Update text, setup the team, and update the display values
                    entry.setText(line);
                    entry.setup();
                    entry.send(this.playerBoard.getBoardStyle().isDecending() ? cache-- : cache++);
                }
            }

            if (player.getScoreboard() != scoreboard) {
                player.setScoreboard(scoreboard);
            }
        }
    }
}
