package me.skeltal.bunkers.util.scoreboard.nametag;

import me.skeltal.bunkers.Bunkers;
import me.skeltal.bunkers.util.scoreboard.nametag.reflect.ReflectionUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

public class ScoreboardTeamPacketMod {
	
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;
    private static Class<?> packetType;
    private Object packet;
    
    public ScoreboardTeamPacketMod(String name, String prefix, String suffix, Collection players, int paramInt) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException {
        super();
        this.packet = ScoreboardTeamPacketMod.packetType.newInstance();
        this.setField("a", name);
        this.setField("f", paramInt);
        if (paramInt == 0 || paramInt == 2) {
            this.setField("b", name);
            this.setField("c", prefix);
            this.setField("d", suffix);
            this.setField("g", 3);
        }
        if (paramInt == 0) {
            this.addAll(players);
        }
    }
    
    public ScoreboardTeamPacketMod(String name, Collection players, int paramInt) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException {
        super();
        this.packet = ScoreboardTeamPacketMod.packetType.newInstance();
        if (paramInt != 3 && paramInt != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        if (players == null || players.isEmpty()) {
            players = new ArrayList<Object>();
        }
        this.setField("g", 3);
        this.setField("a", name);
        this.setField("f", paramInt);
        this.addAll(players);
    }
    
    public void sendToPlayer(Player bukkitPlayer) {
        try {
            Object player = ScoreboardTeamPacketMod.getHandle.invoke(bukkitPlayer, new Object[0]);
            Object connection = ScoreboardTeamPacketMod.playerConnection.get(player);
            ScoreboardTeamPacketMod.sendPacket.invoke(connection, this.packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setField(String field, Object value) {
        try {
            Field f = this.packet.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(this.packet, value);
            f.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void addAll(Collection<?> col) throws NoSuchFieldException, IllegalAccessException {
        Field f = this.packet.getClass().getDeclaredField("e");
        f.setAccessible(true);
        ((Collection)f.get(this.packet)).addAll(col);
    }
    
    static {
        try {
            ScoreboardTeamPacketMod.packetType = Class.forName(ReflectionUtil.getPacketTeamClasspath());
            Class<?> typeCraftPlayer = Class.forName(ReflectionUtil.getCraftPlayerClasspath());
            Class<?> typeNMSPlayer = Class.forName(ReflectionUtil.getNMSPlayerClasspath());
            Class<?> typePlayerConnection = Class.forName(ReflectionUtil.getPlayerConnectionClasspath());
            ScoreboardTeamPacketMod.getHandle = typeCraftPlayer.getMethod("getHandle", (Class<?>[])new Class[0]);
            ScoreboardTeamPacketMod.playerConnection = typeNMSPlayer.getField("playerConnection");
            ScoreboardTeamPacketMod.sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName(ReflectionUtil.getPacketClasspath()));
        } catch (Exception e) {
            Bunkers.getInstance().getLogger().log(Level.WARNING, "Failed to setup reflection for Packet209Mod");
            e.printStackTrace();
        }
    }
}
