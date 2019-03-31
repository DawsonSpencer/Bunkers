package me.skeltal.bunkers.game.listeners;

import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.api.event.update.UpdateEvent;
import me.skeltal.bunkers.api.event.update.UpdateType;
import me.skeltal.bunkers.game.Game;
import me.skeltal.bunkers.game.struct.GameMap;
import me.skeltal.bunkers.game.struct.GameStage;
import me.skeltal.bunkers.game.struct.Team;
import me.skeltal.bunkers.profile.GameProfile;
import me.skeltal.bunkers.timer.TimerManager;
import me.skeltal.bunkers.timer.type.ServerTimer;
import me.skeltal.bunkers.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GameListeners implements Listener {

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        TimerManager timerManager = Bunkers.getInstance().getTimerManager();
        UpdateType type = event.getType();
        Game game = Bunkers.getInstance().getGame();

        if (game != null) {
            switch (game.getGameStage()) {
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
                        if (timerManager.hasTimer(ServerTimer.ServerTimerType.COUNTDOWN)) {
                            ServerTimer timer = timerManager.getTimer(ServerTimer.ServerTimerType.COUNTDOWN);
                            if (Bukkit.getOnlinePlayers().size() < 4) {
                                timerManager.removeTimer(ServerTimer.ServerTimerType.COUNTDOWN);

                                game.setGameStage(GameStage.WAITING);
                                Bukkit.broadcastMessage(CC.translate("&cNot enough players to start the game!"));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.getInventory().setItem(1, Team.RED.getItemStack());
                                    player.getInventory().setItem(3, Team.BLUE.getItemStack());
                                    player.getInventory().setItem(5, Team.GREEN.getItemStack());
                                    player.getInventory().setItem(7, Team.YELLOW.getItemStack());

                                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0f, 1.0f);
                                }
                                return;
                            }

                            Bukkit.broadcastMessage(CC.translate("&eGame starting in &6" + timer.getTimeLeft() + "&e!"));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                            }
                        }
                    }
                    break;
                }
                case ENDING: {
                    if (type == UpdateType.SEC) {
                        Bukkit.broadcastMessage(CC.translate("&6&lGame Over!"));
                        if (game.getWinners() != null) {
                            Bukkit.broadcastMessage(CC.translate("&6Winners: " + game.getWinners().getDisplayName()));
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

        if (game.getGameStage() == GameStage.PLAYING) {
            // todo
        } else {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Game game = Bunkers.getInstance().getGame();

        if (game.getGameStage() == GameStage.PLAYING) {
            // restrict placement (claims, spawn etc.)
        } else {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (Bunkers.getInstance().getGame().getGameStage() != GameStage.PLAYING) {
            event.setCancelled(true);
        } else {
            if (event instanceof EntityDamageByEntityEvent) {
                if (((EntityDamageByEntityEvent) event).getDamager() instanceof Player && event.getEntity() instanceof Player) {
                    GameProfile damagedProfile = GameProfile.getByUuid(event.getEntity().getUniqueId());
                    GameProfile damagerProfile = GameProfile.getByUuid(((EntityDamageByEntityEvent) event).getDamager().getUniqueId());

                    if (damagedProfile.getTeam() == damagerProfile.getTeam()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (!Bunkers.getInstance().getRootConfig().getConfiguration().getBoolean("settings.use-dynamic-motd")) return;
        Game game = Bunkers.getInstance().getGame();
        String prefix = "&6&lBunkers &7" + CC.UNICODE_VERTICAL_BAR + "&f ";

        if (game == null) {
            event.setMotd(CC.translate(prefix + "No Game Running!"));
        } else {
            switch (game.getGameStage()) {
                case WAITING: {
                    event.setMotd(CC.translate(
                            prefix + "Need more players! &7(" + Bukkit.getOnlinePlayers().size() + "/4)\n" +
                                    "&6Game GameStage: &e" + game.getGameStage().getDisplay())
                    );
                    break;
                }
                case COUNTDOWN: {
                    event.setMotd(CC.translate(
                            prefix + "Starting in " + /*game.getCountdown() +*/ " seconds..." +
                                    "\n&6Game GameStage: &e" + game.getGameStage().getDisplay())
                    );
                    break;
                }
                case PLAYING: {
                    event.setMotd(CC.translate(
                            prefix + "Game in progress..." +
                                    "\n&6Game GameStage: &e" + game.getGameStage().getDisplay())
                    );
                    break;
                }
                case ENDING: {
                    event.setMotd(CC.translate(
                            prefix + "Game over!" + (game.getWinners() == null ? "" : "Winners: " + game.getWinners().getDisplayName()) +
                                    "\n&6Game GameStage: &e" + game.getGameStage().getDisplay()));
                    break;
                }
            }
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

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

}
