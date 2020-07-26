package mineplex.core.pet.event;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetSpawnEvent extends Event implements Cancellable
{
  private static final HandlerList handlers = new HandlerList();
  private boolean _cancelled = false;
  
  private Player _player;
  private EntityType _entityType;
  private Location _location;
  
  public PetSpawnEvent(Player player, EntityType entityType, Location location)
  {
    this._player = player;
    this._entityType = entityType;
    this._location = location;
  }
  
  public Player GetPlayer()
  {
    return this._player;
  }
  
  public EntityType GetEntityType()
  {
    return this._entityType;
  }
  
  public Location GetLocation()
  {
    return this._location;
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
}
