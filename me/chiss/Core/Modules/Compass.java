package me.chiss.Core.Modules;

import java.util.HashMap;
import java.util.HashSet;
import me.chiss.Core.Module.AModule;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilWorld;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Compass extends AModule
{
  private HashMap<Player, Entity> _searchMap = new HashMap();
  
  public Compass(JavaPlugin plugin)
  {
    super("Compass", plugin);
  }
  




  public void enable() {}
  




  public void disable() {}
  



  public void config() {}
  



  public void commands()
  {
    AddCommand("compass");
    AddCommand("find");
    AddCommand("q");
  }
  


  public void command(Player caller, String cmd, String[] args)
  {
    if (!caller.getInventory().contains(Material.COMPASS))
    {
      UtilPlayer.message(caller, F.main(this._moduleName, "You do not have a Compass."));
      return;
    }
    

    if (args.length < 1)
    {
      UtilPlayer.message(caller, F.main(this._moduleName, "Missing Entity Parameter."));
      return;
    }
    

    if (UtilEnt.searchName(null, args[0], true) != null) {
      findCreature(caller, UtilEnt.searchName(null, args[0], false));
    }
    else if (args[0].equalsIgnoreCase("random")) {
      findRandom(caller);
    }
    else {
      findPlayer(caller, args[0]);
    }
  }
  










  public void findRandom(Player paramPlayer)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Clans() from the type AModule refers to the missing type Clans\n");
  }
  














  public void findCreature(Player caller, String name)
  {
    EntityType type = UtilEnt.searchEntity(null, name, false);
    

    Entity bestTarget = null;
    double bestDist = 999999.0D;
    

    for (Entity ent : caller.getWorld().getEntities()) {
      if (ent.getType() == type)
      {
        double newDist = closer(caller, ent, bestDist);
        if (newDist < bestDist)
        {
          bestTarget = ent;
          bestDist = newDist;
        }
      }
    }
    
    if (bestTarget == null)
    {
      UtilPlayer.message(caller, F.main(this._moduleName, "There are no " + F.elem(name) + " nearby."));
      setTarget(caller, caller);
      return;
    }
    

    UtilPlayer.message(caller, F.main(this._moduleName, "The nearest " + F.elem(name) + " is at " + F.elem(UtilWorld.locToStrClean(bestTarget.getLocation())) + "."));
    

    setTarget(caller, bestTarget);
  }
  

  public double closer(Player caller, Entity newEnt, double oldDist)
  {
    double newDist = caller.getLocation().toVector().subtract(newEnt.getLocation().toVector()).length();
    
    if (newDist < oldDist) {
      return newDist;
    }
    return 999999999.0D;
  }
  
  public void findPlayer(Player caller, String name)
  {
    Player target = UtilPlayer.searchOnline(caller, name, true);
    
    if (target == null) {
      return;
    }
    if (caller.getLocation().getWorld().equals(target.getLocation().getWorld()))
    {

      UtilPlayer.message(caller, F.main(this._moduleName, F.name(target.getName()) + " is in " + 
        F.elem(UtilWorld.envToStr(caller.getLocation().getWorld().getEnvironment())) + 
        " at " + F.elem(UtilWorld.locToStrClean(target.getLocation())) + "."));
      

      setTarget(caller, target);

    }
    else
    {
      UtilPlayer.message(caller, F.main(this._moduleName, F.name(target.getName()) + " was last seen in " + 
        F.elem(UtilWorld.envToStr(caller.getLocation().getWorld().getEnvironment())) + "."));
      

      setTarget(caller, caller);
    }
  }
  
  public void setTarget(Player caller, Entity target)
  {
    this._searchMap.put(caller, target);
  }
  
  @EventHandler
  public void update(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    for (Player cur : mineplex.core.common.util.UtilServer.getPlayers()) {
      updateCompass(cur);
    }
    updateTarget();
  }
  
  @EventHandler
  public void Quit(PlayerQuitEvent event)
  {
    this._searchMap.remove(event.getPlayer());
  }
  

  public void updateCompass(Player player)
  {
    if (!player.getInventory().contains(Material.COMPASS))
    {
      this._searchMap.remove(player);
      return;
    }
    

    if (!this._searchMap.containsKey(player)) {
      setTarget(player, player);
    }
    
    if (((Entity)this._searchMap.get(player)).equals(player))
    {
      double x = player.getLocation().getX() + Math.sin(System.currentTimeMillis() / 800L % 360.0D) * 20.0D;
      double y = player.getLocation().getY();
      double z = player.getLocation().getZ() + Math.cos(System.currentTimeMillis() / 800L % 360.0D) * 20.0D;
      player.setCompassTarget(new Location(player.getWorld(), x, y, z));
      return;
    }
    

    Entity target = (Entity)this._searchMap.get(player);
    

    if (!player.getLocation().getWorld().equals(target.getLocation().getWorld()))
    {
      UtilPlayer.message(player, F.main(this._moduleName, "Target is no longer in your World."));
      setTarget(player, player);
      return;
    }
    

    player.setCompassTarget(target.getLocation());
  }
  
  public void updateTarget()
  {
    HashSet<Player> toRemove = new HashSet();
    
    for (Player cur : this._searchMap.keySet())
    {

      if ((this._searchMap.get(cur) instanceof LivingEntity))
      {
        LivingEntity ent = (LivingEntity)this._searchMap.get(cur);
        if (ent.isDead())
        {
          toRemove.add(cur);
          UtilPlayer.message(cur, F.main(this._moduleName, "Target has been killed."));
          continue;
        }
      }
      

      if ((this._searchMap.get(cur) instanceof Player))
      {
        Player ent = (Player)this._searchMap.get(cur);
        if (!ent.isOnline())
        {
          toRemove.add(cur);
          UtilPlayer.message(cur, F.main(this._moduleName, "Target has left the game."));
        }
      }
    }
    

    for (Player cur : toRemove) {
      setTarget(cur, cur);
    }
  }
}
