package me.skeltal.bunkers.command;

import com.qrakn.honcho.command.CommandMeta;
import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.game.Game;
import me.skeltal.bunkers.game.struct.Team;
import me.skeltal.bunkers.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "gi", "bi" })
public class GameInfoCommand {

    public void execute(CommandSender sender) {
        Game game = Bunkers.getInstance().getGame();

        sender.sendMessage(CC.translate("&6&nGame Info"));
        sender.sendMessage(CC.translate("&6Duration: "));
        sender.sendMessage(CC.translate("&6Stage: " + game.getStage().getDisplay()));
        //sender.sendMessage(CC.translate("&6Spectators: " + game.getSpectators().size()));
        sender.sendMessage(CC.translate("&6Teams: "));
        for (Team team : Team.values()) {
            sender.sendMessage(CC.translate(" " + team.getDisplayName() + "&r:"));
            sender.sendMessage(CC.translate("  &6Total Balance: &r$"));
            sender.sendMessage(CC.translate("  &6Members &7(" + team.getMembers().size() + "/1)"));
            //  // TODO:                                print members (online/offline/alive/dead)
        }
    }

}
