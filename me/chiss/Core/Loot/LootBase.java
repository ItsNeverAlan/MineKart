package me.chiss.Core.Loot;

import java.util.HashSet;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;





public abstract class LootBase
  implements Listener
{
  protected LootFactory Factory;
  private String _name;
  private String[] _lore;
  private Material _mat;
  private UtilEvent.ActionType _trigger;
  private int _enchLevel;
  private HashSet<Player> _users;
  
  public LootBase(LootFactory factory, String name, String[] lore, Material mat, UtilEvent.ActionType trigger, int level)
  {
    this.Factory = factory;
    
    this._name = name;
    this._lore = lore;
    this._mat = mat;
    this._trigger = trigger;
    
    this._enchLevel = level;
    
    this._users = new HashSet();
  }
  
  @EventHandler
  public void EquipHeld(PlayerItemHeldEvent event)
  {
    ItemStack newSlot = event.getPlayer().getInventory().getItem(event.getNewSlot());
    
    if (!this._users.contains(event.getPlayer())) {
      Equip(event.getPlayer(), newSlot);
    }
  }
  
  @EventHandler
  public void EquipPickup(PlayerPickupItemEvent event) {
    if (event.isCancelled()) {
      return;
    }
    Equip(event.getPlayer(), event.getPlayer().getItemInHand());
  }
  
  @EventHandler
  public void EquipInventory(InventoryCloseEvent event)
  {
    if ((event.getPlayer() instanceof Player)) {
      Equip((Player)event.getPlayer(), event.getPlayer().getItemInHand());
    }
  }
  
  @EventHandler
  public void EquipJoin(PlayerJoinEvent event) {
    Equip(event.getPlayer(), event.getPlayer().getItemInHand());
  }
  



  public void Equip(Player paramPlayer, ItemStack paramItemStack)
  {
    throw new Error("Unresolved compilation problem: \n\tUtilGear cannot be resolved\n");
  }
  






  @EventHandler
  public void UpdateUnequip(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    HashSet<Entity> remove = new HashSet();
    
    for (Entity cur : this._users)
    {
      if ((cur instanceof Player))
      {

        if (Unequip((Player)cur))
          remove.add(cur);
      }
    }
    for (Entity cur : remove) {
      this._users.remove(cur);
    }
  }
  
  public boolean Unequip(Player paramPlayer) {
    throw new Error("Unresolved compilation problem: \n\tUtilGear cannot be resolved\n");
  }
  






  @EventHandler
  public void DamageTrigger(CustomDamageEvent event)
  {
    if (event.IsCancelled()) {
      return;
    }
    if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
      return;
    }
    Player damager = event.GetDamagerPlayer(false);
    if (damager == null) { return;
    }
    if (!this._users.contains(damager)) {
      return;
    }
    Damage(event);
  }
  
  public abstract void Damage(CustomDamageEvent paramCustomDamageEvent);
  
  @EventHandler
  public void AbilityTrigger(PlayerInteractEvent paramPlayerInteractEvent)
  {
    throw new Error("Unresolved compilation problem: \n\tUtilEvent cannot be resolved\n");
  }
  






  public abstract void Ability(PlayerInteractEvent paramPlayerInteractEvent);
  






  public String GetName()
  {
    return this._name;
  }
  
  public Material GetMaterial()
  {
    return this._mat;
  }
  
  public HashSet<Player> GetUsers()
  {
    return this._users;
  }
  
  public void GiveTo(Player caller)
  {
    caller.getInventory().addItem(new ItemStack[] { Get() });
  }
  
  public ItemStack Get()
  {
    ItemStack loot = ItemStackFactory.Instance.CreateStack(this._mat.getId(), (byte)0, 1, "§r" + ChatColor.RESET + C.mLoot + this._name, this._lore);
    
    loot.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, this._enchLevel);
    
    return loot;
  }
  
  public void Reset(Player player)
  {
    this._users.remove(player);
    ResetCustom(player);
  }
  
  public abstract void ResetCustom(Player paramPlayer);
}
