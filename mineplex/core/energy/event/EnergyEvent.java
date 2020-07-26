package mineplex.core.energy.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EnergyEvent extends Event implements Cancellable
{
  public static enum EnergyChangeReason
  {
    Recharge, 
    Use;
  }
  
  private static final HandlerList handlers = new HandlerList();
  private boolean _cancelled = false;
  
  private Player _player;
  private double _amount;
  private double _mods;
  private EnergyChangeReason _reason;
  
  public EnergyEvent(Player player, double amount, EnergyChangeReason reason)
  {
    this._player = player;
    this._amount = amount;
    this._reason = reason;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
  

  public boolean isCancelled()
  {
    return this._cancelled;
  }
  

  public void setCancelled(boolean cancel)
  {
    this._cancelled = cancel;
  }
  
  public Player GetPlayer()
  {
    return this._player;
  }
  
  public double GetAmount()
  {
    return this._amount;
  }
  
  public void AddMod(double mod)
  {
    this._mods += mod;
  }
  
  public double GetTotalAmount()
  {
    return this._amount + this._mods;
  }
  
  public EnergyChangeReason GetReason()
  {
    return this._reason;
  }
}
