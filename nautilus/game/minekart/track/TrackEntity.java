package nautilus.game.minekart.track;

import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilTime;
import nautilus.game.minekart.kart.Kart;
import net.minecraft.server.v1_6_R3.EntityCreature;
import net.minecraft.server.v1_6_R3.Navigation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;



public abstract class TrackEntity
{
  public Track Track;
  private String _name;
  private Entity _ent;
  private EntityType _type;
  private Location _loc;
  private double _offset = 3.0D;
  private double _collideRange = 1.0D;
  
  private long _spawnRate = 30000L;
  private long _spawnTimer = 0L;
  
  public TrackEntity(Track track, EntityType type, String name, double offset, double collideRange, long spawnRate, Location loc)
  {
    this.Track = track;
    
    this._name = name;
    
    this._type = type;
    
    this._spawnRate = spawnRate;
    
    this._offset = offset;
    this._collideRange = collideRange;
    
    this._loc = loc;
  }
  
  public String GetName()
  {
    return this._name;
  }
  
  public Entity GetEntity()
  {
    return this._ent;
  }
  
  public void SetEntity(Entity ent)
  {
    this._ent = ent;
  }
  
  public EntityType GetType()
  {
    return this._type;
  }
  
  public Location GetLocation()
  {
    return this._loc;
  }
  
  public long GetSpawnRate()
  {
    return this._spawnRate;
  }
  
  public long GetSpawnTimer()
  {
    return this._spawnTimer;
  }
  
  public void SetSpawnTimer(long time)
  {
    this._spawnTimer = time;
  }
  
  public double GetOffset()
  {
    return this._offset;
  }
  
  public double GetCollideRange()
  {
    return this._collideRange;
  }
  

  public boolean Update()
  {
    if ((GetEntity() == null) || (!GetEntity().isValid()))
    {
      Respawn();

    }
    else
    {
      Movement();
    }
    
    return false;
  }
  
  public void Respawn()
  {
    if (GetType() == null) {
      return;
    }
    if (GetEntity() != null) {
      GetEntity().remove();
    }
    if (UtilTime.elapsed(GetSpawnTimer(), GetSpawnRate()))
    {
      this._ent = GetLocation().getWorld().spawnEntity(GetLocation(), GetType());
      SetSpawnTimer(System.currentTimeMillis());
    }
  }
  
  public void Movement()
  {
    if (UtilMath.offset(GetLocation(), GetEntity().getLocation()) > GetOffset())
    {
      if ((GetEntity() instanceof Creature))
      {
        EntityCreature ec = ((CraftCreature)GetEntity()).getHandle();
        Navigation nav = ec.getNavigation();
        nav.a(GetLocation().getX(), GetLocation().getY(), GetLocation().getZ(), 0.4000000059604645D);
      }
    }
  }
  
  public void CheckCollision(Kart kart)
  {
    if ((GetEntity() == null) || (!GetEntity().isValid())) {
      return;
    }
    if (UtilMath.offset(kart.GetDriver().getLocation(), GetEntity().getLocation()) > GetCollideRange()) {
      return;
    }
    Collide(kart);
  }
  
  public abstract void Collide(Kart paramKart);
}
