package me.skeltal.bunkers.game.map;

import lombok.Getter;
import lombok.Setter;
import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.game.struct.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.*;

@Getter
public class GameMap {

    @Getter
    public static Set<GameMap> maps = new HashSet<>();
    public static final String MAP_WORLD_PREFIX = "bunkers_map_";

    private String name;
    private World world;
    @Setter private Set<UUID> votes = new HashSet<>();
    private Map<Team, Location> homes = new HashMap<>();

    public GameMap(String name, World world) {
        this.name = name;
        this.world = world;
    }

    public static GameMap getByName(String name) {
        for (GameMap map : maps) {
            if (map.getName().equalsIgnoreCase(name)) {
                return map;
            }
        }
        return null;
    }

    public static void preLoadMaps() {
        for (String mapName : Bunkers.getInstance().getRootConfig().getConfiguration().getStringList("maps")) {
            File worldFolder = new File("./" + MAP_WORLD_PREFIX + mapName).getAbsoluteFile();

            if (!worldFolder.exists()) {
                Bunkers.getInstance().getLogger().warning("Failed to load '" + mapName + "' map because it doesn't exist!");
                return;
            }

            WorldUtils.registerWorld(MAP_WORLD_PREFIX + mapName);
            World world = Bukkit.getWorld(mapName);

            File mapData = new File("./" + MAP_WORLD_PREFIX + mapName + "/map-data.yml").getAbsoluteFile();
            if (!mapData.exists()) {
                Bunkers.getInstance().getLogger().warning("Failed to load '" + mapName + "' map because it doesn't have a map-data file!");
                return;
            }

            // TODO: Load GameMap with all data from map-data.yml
            GameMap gameMap = new GameMap(mapName, world);

            maps.add(gameMap);

            Bunkers.getInstance().getLogger().info("Successfully loaded the '" + mapName + "' map.");
        }
    }

}
