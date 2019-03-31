package me.skeltal.bunkers.game.struct;

import lombok.Getter;
import lombok.Setter;
import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.util.Logger;
import me.skeltal.bunkers.util.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.io.File;
import java.util.*;

@Getter
public class GameMap {

    @Getter
    public static Set<GameMap> maps = new HashSet<>();
    public static final String MAP_WORLD_PREFIX = "bunkers_map_";

    private String name;
    private World world;
    private Set<UUID> votes = new HashSet<>();
    private Map<Team, Location> homes = new HashMap<>();

    public GameMap(String name, World world) {
        this.name = name;
        this.world = world;
    }

    /**
     * Retrieve an instance of the GameMap
     *
     * @param name name of the map you wish to retrieve an instance of
     * @return an instance of the map
     */
    public static GameMap getByName(String name) {
        for (GameMap map : maps) {
            if (map.getName().equalsIgnoreCase(name)) {
                return map;
            }
        }
        return null;
    }

    /**
     * Pre-load all maps (world, attributes, etc.) defined in the plugin configuration
     */
    public static void preLoadMaps() {
        for (String mapName : Bunkers.getInstance().getRootConfig().getConfiguration().getStringList("maps")) {
            File worldFolder = new File("./" + MAP_WORLD_PREFIX + mapName).getAbsoluteFile();

            if (!worldFolder.exists()) {
                Logger.warning("Failed to load '" + mapName + "' map because it doesn't exist!");
                return;
            }

            if (!WorldUtils.isWorldLoaded(mapName)) {
                WorldUtils.registerWorld(MAP_WORLD_PREFIX + mapName);
            }
            World world = Bukkit.getWorld(mapName);

            File mapData = new File("./" + MAP_WORLD_PREFIX + mapName + "/map-data.yml").getAbsoluteFile();
            if (!mapData.exists()) {
                Logger.warning("Failed to load '" + mapName + "' map because it doesn't have a map-data file!");
                return;
            }

            // TODO: Load GameMap with all data from map-data.yml
            GameMap gameMap = new GameMap(mapName, world);

            maps.add(gameMap);

            Logger.info("Successfully loaded the '" + mapName + "' map.");
        }

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                entity.remove();
            }

            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doDayLightCycle", "false");
            world.setTime(0);
        }
    }

}
