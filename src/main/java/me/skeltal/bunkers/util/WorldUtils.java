package me.skeltal.bunkers.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {

    public static void registerWorld(String worldName) {
        Bukkit.getServer().createWorld(new WorldCreator(worldName));
    }

    public static void unloadWorld(String world) {
        Bukkit.getServer().unloadWorld(world, false);
    }

    public static boolean isWorldLoaded(String world) {
        return Bukkit.getWorld(world) != null;
    }

    public static void placeNPC(Location location/*, NPCType type*/) {
        // Spawn NPC
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList();

        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

}
