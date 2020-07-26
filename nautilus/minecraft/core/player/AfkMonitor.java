package nautilus.minecraft.core.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AfkMonitor implements Listener, Runnable
{
  private JavaPlugin _plugin;
  private HashMap<String, Long> _playerList;
  
  public AfkMonitor(JavaPlugin plugin)
  {
    this._plugin = plugin;
    this._playerList = new HashMap();
    
    this._plugin.getServer().getPluginManager().registerEvents(this, this._plugin);
    this._plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this._plugin, this, 0L, 100L);
  }
  
  public void run()
  {
    Iterator<Map.Entry<String, Long>> afkIterator = this._playerList.entrySet().iterator();
    
    while (afkIterator.hasNext())
    {
      Map.Entry<String, Long> entry = (Map.Entry)afkIterator.next();
      
      long timeDiff = System.currentTimeMillis() - ((Long)entry.getValue()).longValue();
      
      if ((timeDiff >= 165000L) && (timeDiff < 180000L))
      {
        Player player = this._plugin.getServer().getPlayerExact((String)entry.getKey());
        
        if ((player != null) && (player.isOnline()))
        {
          player.sendMessage(ChatColor.YELLOW + "You will be marked as AFK in a few seconds...");
        }
      }
      else if (timeDiff >= 180000L)
      {
        this._plugin.getServer().getPluginManager().callEvent(new nautilus.minecraft.core.event.AfkEvent((String)entry.getKey()));
        Player player = this._plugin.getServer().getPlayerExact((String)entry.getKey());
        
        if ((player != null) && (player.isOnline()))
        {
          player.sendMessage(ChatColor.RED + "You have been marked as AFK.");
        }
        
        afkIterator.remove();
      }
    }
  }
  
  @EventHandler
  public void OnPlayerJoin(PlayerJoinEvent event)
  {
    UpdatePlayer(event.getPlayer());
  }
  
  @EventHandler
  public void OnPlayerLeave(PlayerQuitEvent event)
  {
    UpdatePlayer(event.getPlayer());
  }
  
  @EventHandler
  public void OnPlayerMove(PlayerMoveEvent event)
  {
    UpdatePlayer(event.getPlayer());
  }
  
  @EventHandler
  public void OnPlayerChat(AsyncPlayerChatEvent event)
  {
    UpdatePlayer(event.getPlayer());
  }
  
  @EventHandler
  public void OnInventoryClick(InventoryClickEvent event)
  {
    UpdatePlayer((Player)event.getWhoClicked());
  }
  
  @EventHandler
  public void OnInventoryOpen(InventoryOpenEvent event)
  {
    UpdatePlayer((Player)event.getPlayer());
  }
  
  @EventHandler
  public void OnInventoryClose(InventoryCloseEvent event)
  {
    UpdatePlayer((Player)event.getPlayer());
  }
  
  @EventHandler
  public void OnPlayerTeleport(PlayerTeleportEvent event)
  {
    UpdatePlayer(event.getPlayer());
  }
  
  protected void UpdatePlayer(Player player)
  {
    this._playerList.put(player.getName(), Long.valueOf(System.currentTimeMillis()));
  }
}
