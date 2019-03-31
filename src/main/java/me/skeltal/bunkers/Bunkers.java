package me.skeltal.bunkers;

import com.qrakn.honcho.Honcho;
import lombok.Getter;
import me.skeltal.bunkers.api.event.update.UpdateHandler;
import me.skeltal.bunkers.board.BunkersAdapter;
import me.skeltal.bunkers.command.ForceStartCommand;
import me.skeltal.bunkers.command.GameInfoCommand;
import me.skeltal.bunkers.command.VoteCommand;
import me.skeltal.bunkers.database.Mongo;
import me.skeltal.bunkers.game.Game;
import me.skeltal.bunkers.game.listeners.GameListeners;
import me.skeltal.bunkers.game.listeners.PlayerListeners;
import me.skeltal.bunkers.game.listeners.ProfileListeners;
import me.skeltal.bunkers.game.listeners.TimerListeners;
import me.skeltal.bunkers.game.struct.GameMap;
import me.skeltal.bunkers.timer.TimerManager;
import me.skeltal.bunkers.util.config.BukkitConfigHelper;
import me.skeltal.bunkers.util.scoreboard.PlayerBoard;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Bunkers extends JavaPlugin {

    @Getter
    public static Bunkers instance;

    private BukkitConfigHelper rootConfig;

    private Mongo mongo;
    private Honcho honcho;

    private TimerManager timerManager;

    private PlayerBoard board;
    private Game game;

    @Override
    public void onEnable() {
        instance = this;

        this.rootConfig = new BukkitConfigHelper(this, "config", this.getDataFolder().getAbsolutePath());

        this.mongo = new Mongo(this);
        this.honcho = new Honcho(this);

        this.timerManager = new TimerManager(this);

        registerListeners();
        registerCommands();

        this.board = new PlayerBoard(this, new BunkersAdapter());
        setupGame();
    }

    @Override
    public void onDisable() {
        this.mongo.disable();
    }

    private void setupGame() {
        GameMap.preLoadMaps();
        new UpdateHandler(this);
        this.game = new Game();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new ProfileListeners(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        this.getServer().getPluginManager().registerEvents(new GameListeners(), this);
        this.getServer().getPluginManager().registerEvents(new TimerListeners(), this);
    }

    private void registerCommands() {
        honcho.registerCommand(new ForceStartCommand());
        honcho.registerCommand(new VoteCommand());
        honcho.registerCommand(new GameInfoCommand());
    }

}
