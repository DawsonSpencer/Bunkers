package me.skeltal.bunkers.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public final class ItemMaker implements Cloneable {
	
    private Material type;
    private int data;
    private int amount;
    private String title;
    private List<String> lore;
    private Color color;
    private HashMap<Enchantment, Integer> enchantments;
    private boolean unbreakable;
    
    public ItemMaker(Material type) {
        this(type, 1);
    }
    
    public ItemMaker(Material type, int amount) {
        this(type, amount, 0);
    }
    
    public ItemMaker(Material type, int amount, int data) {
        this.lore = new ArrayList<String>();
        this.type = type;
        this.amount = amount;
        this.data = data;
        this.enchantments = new HashMap<Enchantment, Integer>();
    }
    
    public ItemMaker(ItemStack item) {
        this.lore = new ArrayList<String>();
        Validate.notNull((Object)item);
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.type = item.getType();
        this.data = item.getDurability();
        this.amount = item.getAmount();
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName()) {
                this.title = item.getItemMeta().getDisplayName();
            }
            if (item.getItemMeta().hasLore()) {
                this.lore = (List<String>)item.getItemMeta().getLore();
            }
        }
        if (item.getEnchantments() != null) {
            this.enchantments.putAll(item.getEnchantments());
        }
        if (item.getType().toString().toLowerCase().contains("leather") && item.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta lam = (LeatherArmorMeta)item.getItemMeta();
            this.color = lam.getColor();
        }
    }
    
    public ItemMaker(ItemMaker itemMaker) {
        this(itemMaker.build());
    }
    
    public ItemMaker setUnbreakable(boolean flag) {
        this.unbreakable = flag;
        return this;
    }
    
    public ItemMaker addLore(String... lore) {
        for (String s : lore) {
            this.lore.add(CC.translate(s));
        }
        return this;
    }
    
    public ItemMaker setBase64(String base) {
        return this;
    }
    
    public ItemMaker setTexture(String str) {
        return this;
    }
    
    public ItemMaker setData(int data) {
        this.data = data;
        return this;
    }
    
    public ItemMaker setAmount(int amount) {
        this.amount = amount;
        return this;
    }
    
    public ItemMaker setTitle(String title) {
        this.title = CC.translate(title);
        return this;
    }
    
    public ItemMaker setLore(String... lore) {
        this.lore = CC.translateLines(Arrays.asList(lore));
        return this;
    }
    
    public ItemMaker setSkullType(SkullType type) {
        Validate.notNull((Object)type);
        this.setData(type.data);
        return this;
    }
    
    public List<String> getLore() {
        return this.lore;
    }
    
    public ItemMaker setLore(List<String> list) {
        this.lore = CC.translateLines(list);
        return this;
    }
    
    public Material getType() {
        return this.type;
    }
    
    public ItemMaker setType(Material type) {
        this.type = type;
        return this;
    }
    
    public ItemMaker addEnchantment(Enchantment e, int level) {
        this.enchantments.put(e, level);
        return this;
    }
    
    public ItemMaker setColor(Color c) {
        if (!this.type.toString().toLowerCase().contains("leather")) {
            throw new RuntimeException("Cannot set color of non-leather items.");
        }
        this.color = c;
        return this;
    }
    
    public ItemStack build() {
        Validate.noNullElements(new Object[] { this.type, this.data, this.amount });
        ItemStack stack = new ItemStack(this.type, this.amount, (short)this.data);
        ItemMeta im = stack.getItemMeta();
        if (this.title != null && this.title != "") {
            im.setDisplayName(this.title + CC.RESET);
        }
        if (this.lore != null && !this.lore.isEmpty()) {
            im.setLore((List)this.lore);
        }
        if (this.color != null && this.type.toString().toLowerCase().contains("leather")) {
            ((LeatherArmorMeta)im).setColor(this.color);
        }
        stack.setItemMeta(im);
        if (this.enchantments != null && !this.enchantments.isEmpty()) {
            stack.addUnsafeEnchantments((Map)this.enchantments);
        }
        if (this.unbreakable) {
            ItemMeta meta = stack.getItemMeta();
            meta.spigot().setUnbreakable(true);
            stack.setItemMeta(meta);
        }
        return stack;
    }
    
    public ItemMaker clone() {
        return new ItemMaker(this);
    }
    
    public enum SkullType {
    	
        SKELETON("SKELETON", 0, 0), 
        WITHER_SKELETON("WITHER_SKELETON", 1, 1), 
        ZOMBIE("ZOMBIE", 2, 2), 
        PLAYER("PLAYER", 3, 3), 
        CREEPER("CREEPER", 4, 4);
        
        private int data;
        
        private SkullType(String s, int n, int data) {
            this.data = data;
        }
        
        public int getData() {
            return this.data;
        }
    }
}
