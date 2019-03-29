package me.skeltal.bunkers.game.struct;

import lombok.Getter;
import me.skeltal.bunkers.util.CC;
import me.skeltal.bunkers.util.ItemMaker;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public enum Team {

    RED("&c", "Red", new ItemMaker(Material.WOOL).setData(14).setTitle("&c&lRed Team").setLore("&7Right-Click to join this team.").build()),
    BLUE("&9", "Blue", new ItemMaker(Material.WOOL).setData(3).setTitle("&9&lBlue Team").setLore("&7Right-Click to join this team.").build()),
    GREEN("&a", "Green", new ItemMaker(Material.WOOL).setData(5).setTitle("&a&lGreen Team").setLore("&7Right-Click to join this team.").build()),
    YELLOW("&e", "Yellow", new ItemMaker(Material.WOOL).setData(4).setTitle("&e&lYellow Team").setLore("&7Right-Click to join this team.").build());

    private String color, displayName;
    private ItemStack itemStack;
    private Set<UUID> members;

    Team(String color, String displayName, ItemStack itemStack) {
        this.color = CC.translate(color);
        this.displayName = color + displayName;
        this.itemStack = itemStack;

        this.members = new HashSet<>();
    }

}
