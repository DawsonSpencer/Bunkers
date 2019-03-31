package me.skeltal.bunkers.command;

import com.qrakn.honcho.command.CommandMeta;
import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.game.Game;
import me.skeltal.bunkers.game.struct.GameStage;
import me.skeltal.bunkers.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "forcestart" }, permission = "bunkers.admin")
public class ForceStartCommand {

    public void execute(CommandSender sender) {
        Game game = Bunkers.getInstance().getGame();

        if (game == null || game.getGameStage() != GameStage.WAITING) {
            sender.sendMessage(CC.RED + "Couldn't start a new Bunkers game.");
            return;
        }

        game.startCountdown();
    }

}
