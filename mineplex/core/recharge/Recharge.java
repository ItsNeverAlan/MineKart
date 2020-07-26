package mineplex.core.recharge;

import java.util.HashSet;
import java.util.LinkedList;
import mineplex.core.MiniPlugin;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTime.TimeUnit;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Recharge
  extends MiniPlugin
{
  public static Recharge Instance;
  public HashSet<String> informSet = new HashSet();
  public NautHashMap<String, NautHashMap<String, Long>> _recharge = new NautHashMap();
  
  protected Recharge(JavaPlugin plugin)
  {
    super("Recharge", plugin);
  }
  
  public static void Initialize(JavaPlugin plugin)
  {
    Instance = new Recharge(plugin);
  }
  
  @EventHandler
  public void PlayerDeath(PlayerDeathEvent event)
  {
    Get(event.getEntity().getName()).clear();
  }
  
  public NautHashMap<String, Long> Get(String name)
  {
    if (!this._recharge.containsKey(name)) {
      this._recharge.put(name, new NautHashMap());
    }
    return (NautHashMap)this._recharge.get(name);
  }
  
  public NautHashMap<String, Long> Get(Player player)
  {
    return Get(player.getName());
  }
  
  @EventHandler
  public void update(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FASTER) {
      return;
    }
    recharge();
  }
  
  public void recharge()
  {
    for (Player cur : )
    {
      LinkedList<String> rechargeList = new LinkedList();
      

      for (String ability : Get(cur).keySet())
      {
        if (System.currentTimeMillis() > ((Long)Get(cur).get(ability)).longValue()) {
          rechargeList.add(ability);
        }
      }
      
      for (String ability : rechargeList)
      {
        Get(cur).remove(ability);
        

        RechargedEvent rechargedEvent = new RechargedEvent(cur, ability);
        UtilServer.getServer().getPluginManager().callEvent(rechargedEvent);
        
        if (this.informSet.contains(ability))
          UtilPlayer.message(cur, F.main("Recharge", "You can use " + F.skill(ability) + "."));
      }
    }
  }
  
  public boolean use(Player player, String ability, long recharge, boolean inform) {
    return use(player, ability, ability, recharge, inform);
  }
  
  public boolean use(Player player, String ability, String abilityFull, long recharge, boolean inform)
  {
    if (recharge == 0L) {
      return true;
    }
    
    recharge();
    

    if (inform) {
      this.informSet.add(ability);
    }
    
    if (Get(player).containsKey(ability))
    {
      if (inform)
      {
        UtilPlayer.message(player, F.main("Recharge", "You cannot use " + F.skill(abilityFull) + " for " + 
          F.time(UtilTime.convertString(((Long)Get(player).get(ability)).longValue() - System.currentTimeMillis(), 1, UtilTime.TimeUnit.FIT)) + "."));
      }
      
      return false;
    }
    

    UseRecharge(player, ability, recharge);
    
    return true;
  }
  
  public void useForce(Player player, String ability, long recharge)
  {
    UseRecharge(player, ability, recharge);
  }
  
  public boolean usable(Player player, String ability)
  {
    if (!Get(player).containsKey(ability)) {
      return true;
    }
    return System.currentTimeMillis() > ((Long)Get(player).get(ability)).longValue();
  }
  

  public void UseRecharge(Player player, String ability, long recharge)
  {
    RechargeEvent rechargeEvent = new RechargeEvent(player, ability, recharge);
    UtilServer.getServer().getPluginManager().callEvent(rechargeEvent);
    
    Get(player).put(ability, Long.valueOf(System.currentTimeMillis() + rechargeEvent.GetRecharge()));
  }
  
  public void recharge(Player player, String ability)
  {
    Get(player).remove(ability);
  }
  
  @EventHandler
  public void clearPlayer(PlayerQuitEvent event)
  {
    this._recharge.remove(event.getPlayer().getName());
  }
  
  public void Reset(Player player)
  {
    this._recharge.put(player.getName(), new NautHashMap());
  }
}
