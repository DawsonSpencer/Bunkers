package me.skeltal.bunkers.board;

import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.game.Game;
import me.skeltal.bunkers.game.struct.GameMap;
import me.skeltal.bunkers.game.struct.Team;
import me.skeltal.bunkers.profile.GameProfile;
import me.skeltal.bunkers.timer.TimerManager;
import me.skeltal.bunkers.timer.type.ServerTimer;
import me.skeltal.bunkers.util.scoreboard.BoardAdapter;
import me.skeltal.bunkers.util.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BunkersAdapter implements BoardAdapter {

    @Override
    public String getTitle(Player player) {
        return CC.translate("&6&lBunkers");
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> display = new ArrayList<>();
        GameProfile gameProfile = GameProfile.getByUuid(player.getUniqueId());
        Game game = Bunkers.getInstance().getGame();
        TimerManager timerManager = Bunkers.getInstance().getTimerManager();

        switch (game.getGameStage()) {
            case WAITING: {
                if (gameProfile.getTeam() != null) {
                    display.add("&d&lTeam&r: " + gameProfile.getTeam().getDisplayName());
                }

                if (game.getMap() != null) {
                    display.add("&6&lMap: &e" + game.getMap().getName());
                } else {
                    display.add("&6&lMaps:");
                    for (GameMap gameMap : GameMap.maps) {
                        display.add("&7 - &e" + gameMap.getName() + "&r: " + gameMap.getVotes().size());
                    }
                    display.add("");
                }

                for (Team team : Team.values()) {
                    display.add(team.getDisplayName() + "&r (" + team.getMembers().size() + "/1)");
                }
                break;
            }
            case COUNTDOWN: {
                display.add("&d&lTeam&r: " + gameProfile.getTeam().getDisplayName());
                break;
            }
            case PLAYING: {
                display.add("&d&lTeam&r: " + gameProfile.getTeam().getDisplayName());
                display.add("&9&lBalance&r: &c$");
                break;
            }
            case ENDING: {
                display.add("&6&lWinners: &r" + (game.getWinners() == null ? "None" : game.getWinners().getDisplayName()));
                break;
            }
        }

        if (display.size() > 0) {
            display.add(0, CC.BORDER_LINE_SCOREBOARD);
            display.add(CC.BORDER_LINE_SCOREBOARD);
        }

        return display;
    }

}
