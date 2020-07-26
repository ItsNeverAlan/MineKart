package me.chiss.Core.Loot;

import java.util.ArrayList;
import java.util.HashMap;
import me.chiss.Core.Loot.Weapons.AlligatorsTooth;
import me.chiss.Core.Loot.Weapons.GiantsSword;
import me.chiss.Core.Loot.Weapons.HyperAxe;
import me.chiss.Core.Loot.Weapons.LightningScythe;
import me.chiss.Core.Loot.Weapons.MagneticBlade;
import me.chiss.Core.Loot.Weapons.MeteorBow;
import me.chiss.Core.Loot.Weapons.WindBlade;
import me.chiss.Core.Module.AModule;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilWorld;
import mineplex.core.itemstack.ItemStackFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class LootFactory
  extends AModule
{
  private ArrayList<LootBase> _legendary;
  private HashMap<Item, LootBase> _loot = new HashMap();
  
  public LootFactory(JavaPlugin plugin)
  {
    super("Loot Factory", plugin);
  }
  

  public void enable()
  {
    this._legendary = new ArrayList();
    
    AddLoot(new WindBlade(this));
    AddLoot(new LightningScythe(this));
    AddLoot(new HyperAxe(this));
    AddLoot(new GiantsSword(this));
    AddLoot(new MeteorBow(this));
    AddLoot(new AlligatorsTooth(this));
    AddLoot(new MagneticBlade(this));
  }
  



  public void disable() {}
  



  public void config() {}
  



  public void commands()
  {
    AddCommand("giveloot");
  }
  

  public void command(Player paramPlayer, String paramString, String[] paramArrayOfString)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Clients() is undefined for the type LootFactory\n");
  }
  







  @EventHandler
  public void Interact(PlayerInteractEvent paramPlayerInteractEvent)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Clients() is undefined for the type LootFactory\n");
  }
  








  private void AddLoot(LootBase loot)
  {
    this._legendary.add(loot);
    

    UtilServer.getServer().getPluginManager().registerEvents(loot, Plugin());
  }
  
  @EventHandler
  public void Quit(PlayerQuitEvent event)
  {
    for (LootBase loot : this._legendary) {
      loot.Reset(event.getPlayer());
    }
  }
  
  public void DropLoot(Location loc, int eMin, int eRan, float rareChance, float legendChance, double forceMult)
  {
    DropEmerald(loc, eMin, eRan, forceMult);
    

    if (Math.random() < rareChance) {
      DropRare(loc, forceMult);
    }
    
    if (Math.random() < legendChance) {
      DropLegendary(loc, forceMult);
    }
  }
  
  public void DropEmerald(Location loc, int eMin, int eRan, double forceMult) {
    for (int i = 0; i < eMin + UtilMath.r(eRan + 1); i++)
    {
      Item e = loc.getWorld().dropItemNaturally(loc, ItemStackFactory.Instance.CreateStack(Material.EMERALD));
      e.setVelocity(e.getVelocity().multiply(forceMult));
    }
  }
  


  public void DropRare(Location loc, double forceMult) {}
  

  public void DropLegendary(Location loc, double forceMult)
  {
    LootBase loot = (LootBase)this._legendary.get(UtilMath.r(this._legendary.size()));
    Item e = loc.getWorld().dropItemNaturally(loc, loot.Get());
    e.setVelocity(e.getVelocity().multiply(forceMult));
    
    this._loot.put(e, loot);
  }
  
  @EventHandler
  public void Pickup(PlayerPickupItemEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if (!this._loot.containsKey(event.getItem())) {
      return;
    }
    LootBase loot = (LootBase)this._loot.remove(event.getItem());
    
    UtilServer.broadcastSpecial("Legendary Loot", 
      F.name(event.getPlayer().getName()) + " looted " + F.item(new StringBuilder(String.valueOf(C.cRed)).append(loot.GetName()).toString()) + " near " + 
      F.elem(UtilWorld.locToStrClean(event.getPlayer().getLocation())));
  }
}
