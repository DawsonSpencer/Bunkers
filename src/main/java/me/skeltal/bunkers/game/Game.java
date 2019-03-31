package me.skeltal.bunkers.game;

import lombok.Getter;
import lombok.Setter;
import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.game.struct.GameMap;
import me.skeltal.bunkers.game.struct.GameStage;
import me.skeltal.bunkers.game.struct.Team;
import me.skeltal.bunkers.profile.GameProfile;
import me.skeltal.bunkers.timer.TimerManager;
import me.skeltal.bunkers.timer.type.ServerTimer;
import me.skeltal.bunkers.util.CC;
import me.skeltal.bunkers.util.Duration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class Game {

    private Set<GameProfile> players = new HashSet<>();
    @Setter private GameStage gameStage = GameStage.WAITING;
    @Setter private GameMap map = null;
    @Setter private Team winners = null;

    /**
     * Begin the 'Countdown' gameStage of the game
     */
    public void startCountdown() {
        this.setGameStage(GameStage.COUNTDOWN);

        TimerManager timerManager = Bunkers.getInstance().getTimerManager();
        long duration = Duration.SECOND.getDuration() * 7;
        timerManager.addTimer(ServerTimer.ServerTimerType.COUNTDOWN, duration);

        // find map that was highest voted
        //this.setMap(voted);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            GameProfile gameProfile = GameProfile.getByUuid(player.getUniqueId());

            if (gameProfile.getTeam() == null) {
                // find team to put in that isnt full
            }
        }
    }

    /**
     * Begin the 'Playing' gameStage of the game
     */
    public void start() {
        this.setGameStage(GameStage.PLAYING);
        // start the game
        Bukkit.broadcastMessage(CC.translate("&eThe game has now begun, good luck!"));
    }

}
