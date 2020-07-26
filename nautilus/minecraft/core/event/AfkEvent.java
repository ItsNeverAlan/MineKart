package nautilus.minecraft.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AfkEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  private String _playerName;
  
  public AfkEvent(String playerName)
  {
    this._playerName = playerName;
  }
  
  public String GetPlayerName()
  {
    return this._playerName;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
}
