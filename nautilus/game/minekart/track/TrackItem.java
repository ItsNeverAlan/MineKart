package nautilus.game.minekart.track;

import nautilus.game.minekart.kart.Kart;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;


public class TrackItem
{
  private Location _loc;
  private Entity _ent = null;
  private long _delay = 0L;
  
  public TrackItem(Location loc)
  {
    this._loc = loc;
  }
  
  public Location GetLocation()
  {
    return this._loc;
  }
  
  public Entity GetEntity()
  {
    return this._ent;
  }
  
  public long GetDelay()
  {
    return this._delay;
  }
  
  public void SetEntity(Entity ent)
  {
    this._ent = ent;
  }
  
  public void SetDelay(long delay)
  {
    this._delay = delay;
  }
  
  public void SpawnEntity(World world)
  {
    if (GetEntity() != null) {
      GetEntity().remove();
    }
    SetEntity(world.spawnEntity(this._loc.clone().add(0.0D, 0.0D, 0.0D), EntityType.ENDER_CRYSTAL));
  }
  




  public void Pickup(Kart kart)
  {
    if (GetEntity() != null) {
      GetEntity().remove();
    }
    SetEntity(null);
    SetDelay(System.currentTimeMillis());
    
    GetLocation().getWorld().playEffect(GetLocation().clone().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 35);
    
    kart.PickupItem();
  }
}
