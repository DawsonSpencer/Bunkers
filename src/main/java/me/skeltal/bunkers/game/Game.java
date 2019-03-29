package me.skeltal.bunkers.game;

import lombok.Getter;
import lombok.Setter;
import me.skeltal.bunkers.game.map.GameMap;
import me.skeltal.bunkers.game.struct.Stage;
import me.skeltal.bunkers.game.struct.Team;
import me.skeltal.bunkers.profile.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class Game {

    private Set<GameProfile> players;

    @Setter private int countdown;
    @Setter private Stage stage;
    @Setter private GameMap map;
    @Setter private Team winners;

    public Game() {
        this.players = new HashSet<>();

        this.stage = Stage.WAITING;
    }

    public void startCountdown() {
        this.setCountdown(6);
        this.setStage(Stage.COUNTDOWN);

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

    public void start() {
        this.setStage(Stage.PLAYING);

        // start the game
    }

}
