package me.skeltal.bunkers.game.listeners;

import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.profile.GameProfile;
import me.skeltal.bunkers.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class ProfileListeners implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Player player = Bukkit.getServer().getPlayer(event.getUniqueId());

        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.translate("&cYou tried to login too quickly after disconnecting.\n&cTry again in a few seconds."));
            
            Bukkit.getServer().getScheduler().runTask(Bunkers.getInstance(), () -> player.kickPlayer(CC.translate("&cYou logged in from another location.")));
            return;
        }
        
        GameProfile gameProfile = GameProfile.getByUuid(event.getUniqueId());
        try {
            if (!gameProfile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(CC.RED + "Failed to fetch your from the API, try again later.");
                return;
            }
        } catch (Exception e) {
            Bunkers.getInstance().getLogger().log(Level.WARNING, "&cFailed to load GameProfile of " + event.getName());
        }

        if (gameProfile == null || !gameProfile.isLoaded()) {
            event.setKickMessage(CC.RED + "Failed to fetch your from the API, try again later.");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }

        GameProfile.getProfiles().put(gameProfile.getUuid(), gameProfile);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GameProfile gameProfile = GameProfile.getProfiles().remove(player.getUniqueId());

        if (gameProfile != null)
            gameProfile.save();
    }

}
