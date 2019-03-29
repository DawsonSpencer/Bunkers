package me.skeltal.bunkers.api.event.update;

import org.bukkit.plugin.java.JavaPlugin;

public class UpdateHandler implements Runnable {

    private JavaPlugin plugin;

    public UpdateHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 1L);
    }

    @Override
    public void run() {
        for (UpdateType type : UpdateType.values()) {
            if (type.elapsed()) {
                new UpdateEvent(type).call();
            }
        }
    }
}
