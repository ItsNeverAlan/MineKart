package mineplex.core.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import net.minecraft.server.v1_6_R3.DataWatcher;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet205ClientCommand;
import net.minecraft.server.v1_6_R3.Packet24MobSpawn;
import net.minecraft.server.v1_6_R3.Packet29DestroyEntity;
import net.minecraft.server.v1_6_R3.Packet40EntityMetadata;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;




public class UtilDisplay
{
  public static final int ENTITY_ID = 1234;
  private static HashMap<String, Boolean> hasHealthBar = new HashMap();
  
  public static void sendPacket(Player player, Packet packet) {
    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
    
    entityPlayer.playerConnection.sendPacket(packet);
  }
  
  public static Packet24MobSpawn getMobPacket(String text, double healthPercent, Location loc)
  {
    Packet24MobSpawn mobPacket = new Packet24MobSpawn();
    
    mobPacket.a = 1234;
    mobPacket.b = ((byte)EntityType.ENDER_DRAGON.getTypeId());
    mobPacket.c = ((int)Math.floor(loc.getBlockX() * 32.0D));
    mobPacket.d = -200;
    mobPacket.e = ((int)Math.floor(loc.getBlockZ() * 32.0D));
    mobPacket.f = 0;
    mobPacket.g = 0;
    mobPacket.h = 0;
    mobPacket.i = 0;
    mobPacket.j = 0;
    mobPacket.k = 0;
    
    DataWatcher watcher = getWatcher(text, healthPercent * 200.0D);
    try
    {
      Field t = Packet24MobSpawn.class.getDeclaredField("t");
      
      t.setAccessible(true);
      t.set(mobPacket, watcher);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return mobPacket;
  }
  
  public static Packet29DestroyEntity getDestroyEntityPacket() {
    Packet29DestroyEntity packet = new Packet29DestroyEntity();
    
    packet.a = new int[] { 1234 };
    
    return packet;
  }
  
  public static Packet40EntityMetadata getMetadataPacket(DataWatcher watcher) {
    Packet40EntityMetadata metaPacket = new Packet40EntityMetadata();
    
    metaPacket.a = 1234;
    try
    {
      Field b = Packet40EntityMetadata.class.getDeclaredField("b");
      
      b.setAccessible(true);
      b.set(metaPacket, watcher.c());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return metaPacket;
  }
  
  public static Packet205ClientCommand getRespawnPacket() {
    Packet205ClientCommand packet = new Packet205ClientCommand();
    
    packet.a = 1;
    
    return packet;
  }
  
  public static DataWatcher getWatcher(String text, double health) {
    DataWatcher watcher = new DataWatcher();
    
    watcher.a(0, Byte.valueOf((byte)32));
    watcher.a(6, Float.valueOf((float)health));
    watcher.a(10, text);
    watcher.a(11, Byte.valueOf((byte)1));
    watcher.a(16, Integer.valueOf((int)health));
    
    return watcher;
  }
  

  public static void displayTextBar(JavaPlugin plugin, Player player, double healthPercent, String text)
  {
    Packet24MobSpawn mobPacket = getMobPacket(text, healthPercent, player.getLocation());
    
    sendPacket(player, mobPacket);
    hasHealthBar.put(player.getName(), Boolean.valueOf(true));
    
    new BukkitRunnable()
    {
      public void run() {
        Packet29DestroyEntity destroyEntityPacket = UtilDisplay.getDestroyEntityPacket();
        
        UtilDisplay.sendPacket(UtilDisplay.this, destroyEntityPacket);
        UtilDisplay.hasHealthBar.put(UtilDisplay.this.getName(), Boolean.valueOf(false));
      }
    }.runTaskLater(plugin, 120L);
  }
  
  public static void displayLoadingBar(final String text, final String completeText, final Player player, final int healthAdd, long delay, boolean loadUp, final JavaPlugin plugin) {
    Packet24MobSpawn mobPacket = getMobPacket(text, 0.0D, player.getLocation());
    
    sendPacket(player, mobPacket);
    hasHealthBar.put(player.getName(), Boolean.valueOf(true));
    
    new BukkitRunnable() {
      int health = ??? ? 0 : 200;
      
      public void run()
      {
        if (this.val$loadUp ? this.health < 200 : this.health > 0) {
          DataWatcher watcher = UtilDisplay.getWatcher(text, this.health);
          Packet40EntityMetadata metaPacket = UtilDisplay.getMetadataPacket(watcher);
          
          UtilDisplay.sendPacket(player, metaPacket);
          
          if (this.val$loadUp) {
            this.health += healthAdd;
          } else {
            this.health -= healthAdd;
          }
        } else {
          DataWatcher watcher = UtilDisplay.getWatcher(text, this.val$loadUp ? 200 : 0);
          Packet40EntityMetadata metaPacket = UtilDisplay.getMetadataPacket(watcher);
          Packet29DestroyEntity destroyEntityPacket = UtilDisplay.getDestroyEntityPacket();
          
          UtilDisplay.sendPacket(player, metaPacket);
          UtilDisplay.sendPacket(player, destroyEntityPacket);
          UtilDisplay.hasHealthBar.put(player.getName(), Boolean.valueOf(false));
          

          Packet24MobSpawn mobPacket = UtilDisplay.getMobPacket(completeText, 100.0D, player.getLocation());
          
          UtilDisplay.sendPacket(player, mobPacket);
          UtilDisplay.hasHealthBar.put(player.getName(), Boolean.valueOf(true));
          
          DataWatcher watcher2 = UtilDisplay.getWatcher(completeText, 200.0D);
          Packet40EntityMetadata metaPacket2 = UtilDisplay.getMetadataPacket(watcher2);
          
          UtilDisplay.sendPacket(player, metaPacket2);
          
          new BukkitRunnable()
          {
            public void run() {
              Packet29DestroyEntity destroyEntityPacket = UtilDisplay.getDestroyEntityPacket();
              
              UtilDisplay.sendPacket(this.val$player, destroyEntityPacket);
              UtilDisplay.hasHealthBar.put(this.val$player.getName(), Boolean.valueOf(false));
            }
          }.runTaskLater(plugin, 40L);
          
          cancel();
        }
      }
    }.runTaskTimer(plugin, delay, delay);
  }
  
  public static void displayLoadingBar(String text, String completeText, Player player, int secondsDelay, boolean loadUp, JavaPlugin plugin) {
    int healthChangePerSecond = 200 / secondsDelay;
    
    displayLoadingBar(text, completeText, player, healthChangePerSecond, 20L, loadUp, plugin);
  }
}
