package me.skeltal.bunkers.util;

import org.bukkit.Bukkit;

public class Logger {

    public static final String PREFIX = "[Bunkers] ";

    public static void info(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(CC.YELLOW + PREFIX + "[INFO] " + msg);
    }

    public static void warning(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(CC.RED + PREFIX + "[WARNING] " + msg);
    }

}
