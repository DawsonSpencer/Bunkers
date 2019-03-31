package me.skeltal.bunkers.profile;

import lombok.Getter;
import lombok.Setter;
import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.game.struct.GameMap;
import me.skeltal.bunkers.game.struct.Team;
import me.skeltal.bunkers.timer.type.PlayerTimer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class GameProfile {

    @Getter
    public static Map<UUID, GameProfile> profiles = new HashMap<>();

    /* Global Data */
    private UUID uuid;
    @Setter
    private int totalKills, totalDeaths, gamesPlayed, gamesWon;

    /* Session Data */
    private List<PlayerTimer> timers = new ArrayList<>();
    @Setter private Team team = null;
    @Setter private int kills = 0;
    @Setter private int deaths = 0;
    @Setter private int balance = 0;
    @Setter private boolean canRespawn = true;
    @Setter private boolean canRejoin = true;
    @Setter private GameMap vote = null;

    private boolean loaded;

    public GameProfile(UUID uuid) {
        this.uuid = uuid;

        /* Load Global Data */
        this.load();
    }

    /**
     * Retrieves a cached instance of GameProfile or creates and returns a new instance.
     *
     * @param uuid the player's uuid
     * @return the player's GameProfile instance
     * */
    public static GameProfile getByUuid(UUID uuid) {
        if (GameProfile.profiles.containsKey(uuid)) {
            return GameProfile.profiles.get(uuid);
        }
        return new GameProfile(uuid);
    }

    /**
     * Loads player's GameProfile data from a Mongo Document
     */
    private void load() {
        Document document = Bunkers.getInstance().getMongo().getProfile(this.uuid);
        if (document != null) {
            this.gamesPlayed = document.getInteger("games_played");
            this.gamesWon = document.getInteger("games_won");
            this.totalKills = document.getInteger("total_kills");
            this.totalDeaths = document.getInteger("total_deaths");
        }
        this.loaded = true;
    }

    /**
     * Saves player's GameProfile data to a Mongo Document
     */
    public void save() {
        Document document = Bunkers.getInstance().getMongo().getProfile(this.uuid);

        if (document == null) {
            document = new Document();
        }

        document.put("uuid", this.uuid.toString());
        document.put("games_played", this.gamesPlayed);
        document.put("games_won", this.gamesWon);
        document.put("total_kills", this.totalKills);
        document.put("total_deaths", this.totalDeaths);

        Bunkers.getInstance().getMongo().replacePlayer(this, document);
    }

    private Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
