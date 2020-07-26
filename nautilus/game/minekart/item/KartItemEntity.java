package nautilus.game.minekart.item;

import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.item.control.Movement;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.track.Track;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;






public abstract class KartItemEntity
{
  public KartItemManager Manager;
  private Track _track;
  private Material _mat;
  private byte _data;
  private KartItemActive _host;
  private Entity _entity;
  private Vector _velocity;
  private long _fireTime;
  private Kart _owner;
  private Kart _target;
  private double _radius = 2.0D;
  
  public KartItemEntity(KartItemManager manager, Kart owner, Location loc, Material mat, byte data)
  {
    this.Manager = manager;
    
    this._owner = owner;
    this._track = owner.GetGP().GetTrack();
    
    this._mat = mat;
    this._data = data;
    
    this._host = null;
    
    Spawn(loc);
    
    manager.RegisterWorldItem(this);
  }
  

  public void Spawn(Location loc)
  {
    SetEntity(loc.getWorld().dropItem(loc.add(0.0D, 0.5D, 0.0D), new ItemStack(this._mat, 1, (short)0, Byte.valueOf(this._data))));
    SetFired();
  }
  
  public void SetEntity(Entity ent)
  {
    this._entity = ent;
  }
  
  public Entity GetEntity()
  {
    return this._entity;
  }
  
  public Material GetMaterial()
  {
    return this._mat;
  }
  
  public void SetRadius(double rad)
  {
    this._radius = rad;
  }
  
  public void SetVelocity(Vector vel)
  {
    this._velocity = vel;
  }
  
  public Vector GetVelocity()
  {
    return this._velocity;
  }
  
  public Vector GetVelocityClone()
  {
    return new Vector(this._velocity.getX(), this._velocity.getY(), this._velocity.getZ());
  }
  
  public Kart GetOwner()
  {
    return this._owner;
  }
  
  public void SetTarget(Kart kart)
  {
    this._target = kart;
  }
  
  public Kart GetTarget()
  {
    return this._target;
  }
  
  public long GetFireTime()
  {
    return this._fireTime;
  }
  
  public void SetFired()
  {
    this._fireTime = System.currentTimeMillis();
  }
  
  public void SetFiredAdd(long time)
  {
    this._fireTime = (System.currentTimeMillis() + time);
  }
  
  public KartItemActive GetHost()
  {
    return this._host;
  }
  
  public void SetHost(KartItemActive host)
  {
    this._host = host;
  }
  
  public double GetRadius()
  {
    return this._radius;
  }
  
  public abstract void CollideHandle(Kart paramKart);
  
  public boolean TickUpdate()
  {
    if (GetHost() != null) {
      return false;
    }
    Movement.Move(this);
    
    return false;
  }
  
  public void Clean()
  {
    if (this._entity == null) {
      return;
    }
    if (this._entity.getPassenger() != null) {
      this._entity.getPassenger().remove();
    }
    this._entity.remove();
  }
  
  public Track GetTrack()
  {
    return this._track;
  }
}
