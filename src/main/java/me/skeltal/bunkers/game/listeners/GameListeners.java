package me.skeltal.bunkers.game.listeners;

import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.api.event.update.UpdateEvent;
import me.skeltal.bunkers.api.event.update.UpdateType;
import me.skeltal.bunkers.game.Game;
import me.skeltal.bunkers.game.map.GameMap;
import me.skeltal.bunkers.game.struct.Stage;
import me.skeltal.bunkers.game.struct.Team;
import me.skeltal.bunkers.util.CC;
import me.skeltal.bunkers.util.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class GameListeners implements Listener {

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        UpdateType type = event.getType();
        Game game = Bunkers.getInstance().getGame();

        if (game != null) {
            switch (game.getStage()) {
                case WAITING: {
                    if (type == UpdateType.TWO_TICK) {
                        if (Bukkit.getServer().getOnlinePlayers().size() >= 4) {
                            Bukkit.broadcastMessage(CC.translate("&aCountdown starting!"));

                            game.startCountdown();
                        }
                    }

                    if (type == UpdateType.HALF_MIN) {
                        if (game.getMap() == null) {
                            Bukkit.broadcastMessage(CC.translate("&eDon't forget to use &6/vote &eto vote for a map!"));
                            for (GameMap map : GameMap.maps) {
                                Bukkit.broadcastMessage(CC.translate("&7 - &e" + map.getName() + "&r: " + map.getVotes().size() + " vote" + (map.getVotes().size() == 1 ? "" : "s")));
                            }
                        }
                    }

                    if (type == UpdateType.TWENTY_SEC) {
                        if (Bukkit.getServer().getOnlinePlayers().size() < 4) {
                            Bukkit.broadcastMessage(CC.translate("&eWaiting for players... &7(" + Bukkit.getOnlinePlayers().size() + "/4)"));
                        }
                    }
                    break;
                }
                case COUNTDOWN: {
                    if (type == UpdateType.SEC) {
                        if (Bukkit.getOnlinePlayers().size() >= 4) {
                            if (game.getCountdown() - 1 == 0) {
                                // Starting the game
                                game.start();
                            } else {
                                // Counting down
                                game.setCountdown(game.getCountdown() - 1);

                                if (game.getCountdown() <= 5) {
                                    Bukkit.broadcastMessage(CC.translate("&eGame starting in &6" + game.getCountdown() + " &eseconds!"));
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                                    }
                                }
                            }
                        } else {
                            game.setStage(Stage.WAITING);
                            Bukkit.broadcastMessage(CC.translate("&cNot enough players to start the game!"));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.getInventory().setItem(1, Team.RED.getItemStack());
                                player.getInventory().setItem(3, Team.BLUE.getItemStack());
                                player.getInventory().setItem(5, Team.GREEN.getItemStack());
                                player.getInventory().setItem(7, Team.YELLOW.getItemStack());

                                player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0f, 1.0f);
                            }
                        }
                    }
                    break;
                }
                case ENDING: {
                    if (type == UpdateType.SEC) {
                        Bukkit.broadcastMessage(CC.translate("&6&lGame Over!"));
                        if (game.getWinners() != null) {
                            Bukkit.broadcastMessage(CC.translate("&6Wiiners: " + game.getWinners().getDisplayName()));
                        }
                    }
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Game game = Bunkers.getInstance().getGame();

        event.setCancelled(true);

        if (game.getStage() == Stage.PLAYING) {
            Block block = event.getBlock();

            if (block.getType().name().contains("ORE")) {
                player.getInventory().addItem(new ItemMaker(block.getType()).build());

                // set to cobble then runTaskAsyncLater and re-set as the ore
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Game game = Bunkers.getInstance().getGame();

        if (game.getStage() == Stage.PLAYING) {
            // restrict placement (claims, spawn etc.)
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        Game game = Bunkers.getInstance().getGame();
        String prefix = "&6&lBunkers &7" + CC.UNICODE_VERTICAL_BAR + "&f ";

        if (game == null) {
            event.setMotd(CC.translate(prefix + "No Game Running!"));
        } else {
            switch (game.getStage()) {
                case WAITING: {
                    event.setMotd(CC.translate(prefix + "Need more players! &7(" + Bukkit.getOnlinePlayers().size() + "/4)\n&6Game Stage: &e" + game.getStage().getDisplay()));
                    break;
                }
                case COUNTDOWN: {
                    event.setMotd(CC.translate(prefix + "Starting in " + game.getCountdown() + " seconds...\n&6Game Stage: &e" + game.getStage().getDisplay()));
                    break;
                }
                case PLAYING: {
                    event.setMotd(CC.translate(prefix + "Game in progress...\n&6Game Stage: &e" + game.getStage().getDisplay()));
                    break;
                }
                case ENDING: {
                    event.setMotd(CC.translate(prefix + "Game over!" + (game.getWinners() == null ? "" : "Winners: " + game.getWinners().getDisplayName()) + "\n&6Game Stage: &e" + game.getStage().getDisplay()));
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().clear();
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBedEnter(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Wither || entity instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        switch (event.getBlock().getType()) {
            case SNOW:
            case ICE:
                event.setCancelled(true);
                break;
            default:
                break;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }

}
