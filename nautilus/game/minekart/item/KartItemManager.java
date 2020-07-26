package nautilus.game.minekart.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import mineplex.core.MiniPlugin;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.minekart.item.control.Collision;
import nautilus.game.minekart.item.control.Movement;
import nautilus.game.minekart.item.use_default.ItemUse;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartManager;
import nautilus.game.minekart.kart.KartType;
import nautilus.game.minekart.kart.crash.Crash_Explode;
import nautilus.game.minekart.track.Track;
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class KartItemManager extends MiniPlugin
{
  public KartManager KartManager;
  private HashMap<Integer, ArrayList<KartItemType>> _itemSelection = new HashMap();
  
  private HashSet<KartItemActive> _kartItems = new HashSet();
  private HashSet<KartItemEntity> _worldItems = new HashSet();
  
  public KartItemManager(JavaPlugin plugin, KartManager kartManager)
  {
    super("Kart Item Manager", plugin);
    
    this.KartManager = kartManager;
  }
  
  @EventHandler
  public void UseItem(PlayerDropItemEvent event)
  {
    Kart kart = this.KartManager.GetKart(event.getPlayer());
    if (kart == null) { return;
    }
    event.setCancelled(true);
    
    KartItemActive active = kart.GetItemActive();
    if (active != null)
    {
      if (active.Use())
      {

        kart.SetItemActive(null);
        

        ItemDecrement(kart);
        
        GetKartItems().remove(active);
        

        kart.GetDriver().playSound(kart.GetDriver().getLocation(), kart.GetKartType().GetSoundMain(), 2.0F, 1.0F);
      }
      
      return;
    }
    
    KartItemType type = kart.GetItemStored();
    if (type != null)
    {
      if (kart.GetItemCycles() > 10)
      {
        kart.SetItemCycles(kart.GetItemCycles() - 3);
        return;
      }
      if (kart.GetItemCycles() <= 0)
      {
        type.GetAction().Use(this, kart);
        

        kart.GetDriver().playSound(kart.GetDriver().getLocation(), kart.GetKartType().GetSoundMain(), 2.0F, 1.0F);
      }
    }
  }
  
  @EventHandler
  public void CancelPickup(PlayerPickupItemEvent event)
  {
    for (KartItemEntity item : GetWorldItems()) {
      if ((item.GetEntity() != null) && (item.GetEntity().equals(event.getItem())))
      {
        event.setCancelled(true);
        return;
      }
    }
    
    event.setCancelled(true);
    event.getItem().remove();
  }
  
  @EventHandler
  public void CancelTarget(EntityTargetEvent event)
  {
    event.setCancelled(true);
  }
  
  @EventHandler
  public void EggHit(EntityDamageEvent event)
  {
    if (!(event instanceof EntityDamageByEntityEvent)) {
      return;
    }
    EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent)event;
    
    if (!(eventEE.getDamager() instanceof Egg)) {
      return;
    }
    Egg egg = (Egg)eventEE.getDamager();
    
    if (egg.getShooter() == null) {
      return;
    }
    if (!(egg.getShooter() instanceof Player)) {
      return;
    }
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    Player damager = (Player)egg.getShooter();
    Player damagee = (Player)event.getEntity();
    
    Kart kart = this.KartManager.GetKart(damagee);
    if (kart == null) { return;
    }
    new Crash_Explode(kart, 0.300000011920929D, false);
    

    if (damager.equals(damagee))
    {
      UtilPlayer.message(damagee, F.main("MK", "You hit yourself with " + F.item("Egg Blaster") + "."));
    }
    else
    {
      UtilPlayer.message(damagee, F.main("MK", F.elem(damager.getName()) + " hit you with " + F.item("Egg Blaster") + "."));
      UtilPlayer.message(damager, F.main("MK", "You hit " + F.elem(damagee.getName()) + " with " + F.item("Egg Blaster") + "."));
    }
  }
  
  @EventHandler
  public void KartItemCycle(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FASTEST) {
      return;
    }
    for (Kart kart : this.KartManager.GetKarts().values())
    {
      if (kart.GetItemCycles() > 0)
      {

        if (kart.GetItemCycles() < 10)
        {

          if (kart.GetItemCycles() % 3 == 0) {
            kart.GetDriver().playSound(kart.GetDriver().getLocation(), Sound.NOTE_PLING, 0.4F, 2.0F);
          }
          
        }
        else
        {
          kart.GetDriver().playSound(kart.GetDriver().getLocation(), Sound.NOTE_PLING, 0.2F, 1.0F + (10 - kart.GetItemCycles() % 10) * 0.05F);
          
          KartItemType next = GetNewItem(kart);
          

          while (next.equals(kart.GetItemStored())) {
            next = GetNewItem(kart);
          }
          kart.SetItemStored(next);
        }
        
        kart.SetItemCycles(kart.GetItemCycles() - 1);
        

        if (kart.GetItemCycles() == 0)
        {
          if ((kart.GetItemStored() == KartItemType.Banana) || 
            (kart.GetItemStored() == KartItemType.BananaBunch) || 
            (kart.GetItemStored() == KartItemType.SingleGreenShell) || 
            (kart.GetItemStored() == KartItemType.DoubleGreenShell) || 
            (kart.GetItemStored() == KartItemType.TripleGreenShell) || 
            (kart.GetItemStored() == KartItemType.SingleRedShell) || 
            (kart.GetItemStored() == KartItemType.DoubleRedShell) || 
            (kart.GetItemStored() == KartItemType.TripleRedShell))
          {
            kart.GetItemStored().GetAction().Use(this, kart);
          }
          
        }
      }
      else if ((kart.GetItemActive() == null) && (kart.GetItemStored() != null))
      {
        ItemDecrement(kart);
      }
    }
  }
  
  public void ItemDecrement(Kart kart)
  {
    if ((kart.GetItemStored() == KartItemType.Banana) || 
      (kart.GetItemStored() == KartItemType.SingleGreenShell) || 
      (kart.GetItemStored() == KartItemType.SingleRedShell))
    {
      kart.SetItemStored(null);
    }
    
    if (kart.GetItemStored() == KartItemType.BananaBunch)
    {
      ItemStack item = kart.GetDriver().getInventory().getItem(3);
      
      if ((item == null) || (item.getAmount() == 1))
      {
        kart.SetItemStored(null);
      }
      else
      {
        item.setAmount(item.getAmount() - 1);
        kart.GetItemStored().GetAction().Use(this, kart);
      }
    }
    
    if (kart.GetItemStored() == KartItemType.DoubleGreenShell)
    {
      kart.SetItemStored(KartItemType.SingleGreenShell);
      kart.GetItemStored().GetAction().Use(this, kart);
    }
    
    if (kart.GetItemStored() == KartItemType.TripleGreenShell)
    {
      kart.SetItemStored(KartItemType.DoubleGreenShell);
      kart.GetItemStored().GetAction().Use(this, kart);
    }
    
    if (kart.GetItemStored() == KartItemType.DoubleRedShell)
    {
      kart.SetItemStored(KartItemType.SingleRedShell);
      kart.GetItemStored().GetAction().Use(this, kart);
    }
    
    if (kart.GetItemStored() == KartItemType.TripleRedShell)
    {
      kart.SetItemStored(KartItemType.DoubleRedShell);
      kart.GetItemStored().GetAction().Use(this, kart);
    }
  }
  
  @EventHandler
  public void KartItemUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    for (KartItemActive active : GetKartItems())
    {
      if (active.GetType() == KartItemActive.ActiveType.Behind) {
        Movement.Behind(active.GetKart(), active.GetEntities());
      }
      if (active.GetType() == KartItemActive.ActiveType.Orbit) {
        Movement.Orbit(active.GetKart(), active.GetEntities());
      }
      if (active.GetType() == KartItemActive.ActiveType.Trail) {
        Movement.Trail(active.GetKart(), active.GetEntities());
      }
    }
  }
  
  @EventHandler
  public void WorldItemUpdate(UpdateEvent event) {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    HashSet<KartItemEntity> remove = new HashSet();
    
    for (KartItemEntity item : GetWorldItems())
    {
      if ((item.GetTrack() == null) || (item.GetTrack().GetWorld() == null))
      {
        remove.add(item);


      }
      else if (!remove.contains(item))
      {

        if (item.TickUpdate()) {
          remove.add(item);
        }
        
        KartItemEntity other = Collision.CollideItem(item, GetWorldItems());
        if (other != null)
        {
          remove.add(item);
          remove.add(other);
        }
        

        if (Collision.CollidePlayer(item, this.KartManager.GetKarts().values()))
          remove.add(item);
      }
    }
    for (KartItemEntity item : remove)
    {
      if (item.GetHost() != null)
      {
        item.GetHost().GetEntities().remove(item);
        
        if (item.GetHost().GetEntities().isEmpty())
        {
          item.GetHost().GetKart().SetItemActive(null);
          GetKartItems().remove(item.GetHost());
        }
      }
      
      item.Clean();
      
      this._worldItems.remove(item);
    }
  }
  
  public void RegisterKartItem(KartItemActive item)
  {
    this._kartItems.add(item);
  }
  
  public void RegisterWorldItem(KartItemEntity item)
  {
    this._worldItems.add(item);
  }
  
  public HashSet<KartItemActive> GetKartItems()
  {
    return this._kartItems;
  }
  
  public HashSet<KartItemEntity> GetWorldItems()
  {
    return this._worldItems;
  }
  
  public KartItemType GetNewItem(Kart kart)
  {
    if (Math.random() > 1.0D - kart.GetKartType().GetKartItem().GetChance()) {
      return kart.GetKartType().GetKartItem();
    }
    int pos = kart.GetLapPlace();
    
    if ((kart.GetGP() instanceof nautilus.game.minekart.gp.GPBattle)) {
      pos = -1;
    }
    if (!this._itemSelection.containsKey(Integer.valueOf(pos))) {
      this._itemSelection.put(Integer.valueOf(pos), KartItemType.GetItem(pos));
    }
    ArrayList<KartItemType> itemBag = (ArrayList)this._itemSelection.get(Integer.valueOf(pos));
    
    return (KartItemType)itemBag.get(UtilMath.r(itemBag.size()));
  }
}
