package me.chiss.Core.Modules;

import java.util.WeakHashMap;
import me.chiss.Core.Module.AModule;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.teleport.Teleport;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.condition.ConditionManager;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Observer extends AModule
{
  private WeakHashMap<Player, Location> _obs = new WeakHashMap();
  
  public Observer(JavaPlugin plugin)
  {
    super("Observer", plugin);
  }
  



  public void enable() {}
  



  public void disable()
  {
    for (Player player : this._obs.keySet()) {
      remove(player, false);
    }
  }
  


  public void config() {}
  


  public void commands()
  {
    AddCommand("o");
    AddCommand("obs");
    AddCommand("observer");
    AddCommand("z");
  }
  

  public void command(Player paramPlayer, String paramString, String[] paramArrayOfString)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Clients() is undefined for the type Observer\n");
  }
  




  @EventHandler
  public void handleJoin(PlayerQuitEvent event)
  {
    event.getPlayer().setGameMode(GameMode.SURVIVAL);
    remove(event.getPlayer(), false);
  }
  
  public void add(Player player, boolean inform)
  {
    this._obs.put(player, player.getLocation());
    player.setGameMode(GameMode.CREATIVE);
    UtilInv.Clear(player);
    player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.WEB, 1));
    
    if (inform) {
      UtilPlayer.message(player, F.main(this._moduleName, "You entered Observer Mode."));
    }
  }
  
  public boolean remove(Player player, boolean inform) {
    Location loc = (Location)this._obs.remove(player);
    if ((loc != null) && (inform))
    {
      UtilPlayer.message(player, F.main(this._moduleName, "You left Observer Mode."));
      player.setGameMode(GameMode.SURVIVAL);
      UtilInv.Clear(player);
      Teleport().TP(player, loc);
      return true;
    }
    
    return false;
  }
  



  public boolean isObserver(Player paramPlayer, boolean paramBoolean)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Clients() is undefined for the type Observer\n");
  }
  







  @EventHandler
  public void update(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    for (Player player : this._obs.keySet()) {
      Condition().Factory().Cloak("Observer", player, player, 1.9D, false, true);
    }
  }
  





  @EventHandler
  public void handleInventoryOpen(InventoryOpenEvent paramInventoryOpenEvent)
  {
    throw new Error("Unresolved compilation problem: \n\tThe method Clients() is undefined for the type Observer\n");
  }
  







  @EventHandler
  public void handleInventoryClick(InventoryClickEvent event)
  {
    if (!this._obs.containsKey(event.getWhoClicked())) {
      return;
    }
    UtilPlayer.message(UtilPlayer.searchExact(event.getWhoClicked().getName()), F.main(this._moduleName, "You cannot interact with Inventory."));
    event.getWhoClicked().closeInventory();
    event.setCancelled(true);
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void handlePickup(PlayerPickupItemEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if (this._obs.containsKey(event.getPlayer())) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void handleDrop(PlayerDropItemEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (this._obs.containsKey(event.getPlayer())) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void handleBlockPlace(BlockPlaceEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (this._obs.containsKey(event.getPlayer())) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void handleBlockBreak(BlockBreakEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (this._obs.containsKey(event.getPlayer())) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void handleDamage(CustomDamageEvent event) {
    Player damager = event.GetDamagerPlayer(true);
    if (damager == null) { return;
    }
    if (this._obs.containsKey(damager)) {
      event.SetCancelled("Observer Mode");
    }
  }
}
