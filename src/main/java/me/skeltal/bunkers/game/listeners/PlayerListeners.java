package me.skeltal.bunkers.game.listeners;

import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.game.Game;
import me.skeltal.bunkers.game.struct.GameStage;
import me.skeltal.bunkers.game.struct.Team;
import me.skeltal.bunkers.profile.GameProfile;
import me.skeltal.bunkers.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Game game = Bunkers.getInstance().getGame();

        switch (game.getGameStage()) {
            case WAITING: {
                player.teleport(player.getWorld().getSpawnLocation());

                player.getInventory().clear();
                player.getInventory().setArmorContents(new ItemStack[4]);

                player.getInventory().setItem(1, Team.RED.getItemStack());
                player.getInventory().setItem(3, Team.BLUE.getItemStack());
                player.getInventory().setItem(5, Team.GREEN.getItemStack());
                player.getInventory().setItem(7, Team.YELLOW.getItemStack());
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        GameProfile gameProfile = GameProfile.getByUuid(player.getUniqueId());
        Game game = Bunkers.getInstance().getGame();

        if (game.getGameStage() == GameStage.WAITING) {
            if (event.getItem() != null && event.getAction().name().contains("RIGHT")) {
                for (Team team : Team.values()) { // todo optimize?
                    if (event.getItem().isSimilar(team.getItemStack())) {
                        if (gameProfile.getTeam() != null && gameProfile.getTeam() == team) {
                            player.sendMessage(CC.RED + "You are already on this team.");
                            return;
                        }
                        if (team.getMembers().size() >= 1) {
                            player.sendMessage(CC.RED + "That team is full.");
                            return;
                        }

                        if (gameProfile.getTeam() != null) {
                            gameProfile.getTeam().getMembers().remove(player.getUniqueId());
                        }
                        player.sendMessage(CC.translate("&7You joined the " + team.getDisplayName() + " &7team!"));
                        team.getMembers().add(player.getUniqueId());
                        gameProfile.setTeam(team);
                    }
                }
            }
        }
    }

}
