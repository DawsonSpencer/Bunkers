package me.skeltal.bunkers.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import me.skeltal.bunkers.game.struct.GameMap;
import me.skeltal.bunkers.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = { "vote" })
public class VoteCommand {
    public void execute(Player player, @CPL(value = "map") String mapName) {
        GameMap gameMap = GameMap.getByName(mapName);

        if (gameMap == null) {
            player.sendMessage(CC.RED + "That is not a valid map!");
            return;
        }

        if (gameMap.getVotes().contains(player.getUniqueId())) {
            player.sendMessage(CC.RED + "You already voted for that map.");
            return;
        }

        for (GameMap maps : GameMap.maps) {
            maps.getVotes().removeIf(uuid -> uuid.equals(player.getUniqueId()));
        }

        // todo     donator votes
        gameMap.getVotes().add(player.getUniqueId());
        player.sendMessage(CC.GREEN + "You voted for '" + gameMap.getName() + "'");
    }
}
