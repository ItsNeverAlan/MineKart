package me.chiss.Core.Server;

import me.chiss.Core.Events.ServerSaveEvent;
import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTime.TimeUnit;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class Server
  extends MiniPlugin
{
  private CoreClientManager _clientManager;
  public boolean stopWeather = true;
  public boolean liquidSpread = true;
  
  public Server(JavaPlugin plugin, CoreClientManager clientManager)
  {
    super("Server", plugin);
    
    this._clientManager = clientManager;
  }
  

  public void AddCommands()
  {
    throw new Error("Unresolved compilation problem: \n\tThe constructor BroadcastCommand(Server) is undefined\n");
  }
  



  @EventHandler
  public void WorldTimeWeather(UpdateEvent event)
  {
    if (event.getType() == UpdateType.TICK)
    {
      for (World cur : UtilServer.getServer().getWorlds())
      {
        if ((cur.getTime() > 12000L) && (cur.getTime() < 24000L)) {
          cur.setTime(cur.getTime() + 5L);
        }
        if ((cur.getTime() > 14000L) && (cur.getTime() < 22000L)) {
          cur.setTime(22000L);
        }
        if (this.stopWeather)
        {
          cur.setStorm(false);
          cur.setThundering(false);
        }
      }
    }
    
    if (event.getType() == UpdateType.MIN_04)
    {
      ServerSaveEvent saveEvent = new ServerSaveEvent();
      this._plugin.getServer().getPluginManager().callEvent(saveEvent);
      
      if (!saveEvent.isCancelled())
      {
        saveClients();
        saveWorlds();
      }
    }
  }
  
  @EventHandler
  public void WaterSpread(BlockFromToEvent event)
  {
    if (!this.liquidSpread) {
      event.setCancelled(true);
    }
  }
  
  public void saveClients() {
    long epoch = System.currentTimeMillis();
    

    for (Player cur : UtilServer.getPlayers()) {
      cur.saveData();
    }
    Log("Saved Clients to Disk. Took " + (System.currentTimeMillis() - epoch) + " milliseconds.");
  }
  
  public void saveWorlds()
  {
    long epoch = System.currentTimeMillis();
    

    for (World cur : UtilServer.getServer().getWorlds()) {
      cur.save();
    }
    UtilServer.broadcast(
    
      C.cGray + "Saved Worlds [" + F.time(UtilTime.convertString(System.currentTimeMillis() - epoch, 1, UtilTime.TimeUnit.FIT)) + "].");
    
    Log("Saved Worlds to Disk. Took " + (System.currentTimeMillis() - epoch) + " milliseconds.");
  }
  
  public void reload()
  {
    UtilServer.broadcast(F.main(this._moduleName, "Reloading Plugins..."));
    Log("Reloading Plugins...");
    UtilServer.getServer().dispatchCommand(UtilServer.getServer().getConsoleSender(), "reload");
  }
  
  public void restart()
  {
    UtilServer.broadcast(F.main(this._moduleName, "Restarting Server..."));
    Log("Restarting Server...");
    
    for (Player cur : UtilServer.getPlayers()) {
      UtilPlayer.kick(cur, this._moduleName, "Server Restarting");
    }
    UtilServer.getServer().dispatchCommand(UtilServer.getServer().getConsoleSender(), "stop");
  }
  
  @EventHandler
  public void handleCommand(PlayerCommandPreprocessEvent event)
  {
    String cmdName = event.getMessage().split("\\s+")[0].substring(1);
    
    if ((cmdName.equalsIgnoreCase("reload")) || (cmdName.equalsIgnoreCase("rl")))
    {
      event.setCancelled(true);
      
      if (this._clientManager.Get(event.getPlayer()).GetRank().Has(event.getPlayer(), Rank.ADMIN, true)) {
        reload();
      }
    } else if (cmdName.equalsIgnoreCase("stop"))
    {
      event.setCancelled(true);
      
      if (this._clientManager.Get(event.getPlayer()).GetRank().Has(event.getPlayer(), Rank.ADMIN, true)) {
        restart();
      }
    } else if (cmdName.equalsIgnoreCase("reload"))
    {
      if (event.getPlayer().getName().equals("Chiss")) {
        return;
      }
      event.getPlayer().sendMessage("Plugins cannot be reloaded.");
      
      event.setCancelled(true);
    }
    else if (cmdName.equalsIgnoreCase("me"))
    {
      event.setCancelled(true);
    }
  }
  
  public CoreClientManager GetClientManager()
  {
    return this._clientManager;
  }
  
  public void ToggleLiquidSpread()
  {
    this.liquidSpread = (!this.liquidSpread);
  }
  
  public boolean GetLiquidSpread()
  {
    return this.liquidSpread;
  }
}
