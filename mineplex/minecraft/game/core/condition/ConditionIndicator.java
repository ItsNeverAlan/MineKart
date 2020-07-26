package mineplex.minecraft.game.core.condition;

import mineplex.core.itemstack.ItemStackFactory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ConditionIndicator implements org.bukkit.event.Listener
{
  private Entity _indicator;
  private Condition _condition;
  
  public ConditionIndicator(Condition condition)
  {
    SetCondition(condition);
  }
  
  public Condition GetCondition()
  {
    return this._condition;
  }
  
  public Entity GetIndicator()
  {
    if (!IsVisible()) {
      return null;
    }
    if (this._indicator == null) {
      this._indicator = this._condition.GetEnt().getWorld().dropItem(this._condition.GetEnt().getEyeLocation(), 
        ItemStackFactory.Instance.CreateStack(this._condition.GetIndicatorMaterial(), this._condition.GetIndicatorData()));
    }
    return this._indicator;
  }
  
  public void SetCondition(Condition newCon)
  {
    this._condition = newCon;
    
    if ((this._indicator != null) && 
      ((this._indicator instanceof Item))) {
      ((Item)this._indicator).getItemStack().setType(newCon.GetIndicatorMaterial());
    }
    newCon.Apply();
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void Pickup(PlayerPickupItemEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if ((this._indicator != null) && 
      (this._indicator.equals(event.getItem()))) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void HopperPickup(InventoryPickupItemEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if ((this._indicator != null) && 
      (this._indicator.equals(event.getItem()))) {
      event.setCancelled(true);
    }
  }
  
  public boolean IsVisible() {
    return GetCondition().IsVisible();
  }
}
