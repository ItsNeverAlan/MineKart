package nautilus.game.minekart.track.ents;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.crash.Crash_Explode;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.TrackEntity;
import net.minecraft.server.v1_6_R3.EntityCreature;
import net.minecraft.server.v1_6_R3.Navigation;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Spiderling extends TrackEntity
{
  private boolean _spawned = false;
  
  private Kart _target;
  private Kart _owner;
  private long _spawn = 0L;
  
  private boolean _dead = false;
  
  public Spiderling(Track track, Location loc, Kart owner, Kart target)
  {
    super(track, org.bukkit.entity.EntityType.CAVE_SPIDER, "Spiderling", 5.0D, 1.0D, 30000L, loc);
    
    this._owner = owner;
    this._target = target;
    
    this._spawn = System.currentTimeMillis();
  }
  

  public void CheckCollision(Kart kart)
  {
    if (kart.equals(this._owner)) {
      return;
    }
    if ((GetEntity() == null) || (!GetEntity().isValid())) {
      return;
    }
    if (mineplex.core.common.util.UtilMath.offset(kart.GetDriver().getLocation(), GetEntity().getLocation()) > GetCollideRange()) {
      return;
    }
    Collide(kart);
  }
  

  public void Collide(Kart kart)
  {
    SetSpawnTimer(System.currentTimeMillis());
    GetEntity().remove();
    
    if (!kart.IsInvulnerable(true))
    {
      UtilPlayer.message(kart.GetDriver(), F.main("MK", F.elem(this._owner.GetDriver().getName()) + " hit you with " + F.item(GetName()) + "."));
      UtilPlayer.message(this._owner.GetDriver(), F.main("MK", "You hit " + F.elem(kart.GetDriver().getName()) + " with " + F.item(GetName()) + "."));
      

      new Crash_Explode(kart, 1.200000047683716D, true);
    }
    

    GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.SPIDER_DEATH, 2.0F, 1.0F);
    GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.SPIDER_DEATH, 2.0F, 1.0F);
    GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.SPIDER_DEATH, 2.0F, 1.0F);
    GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.SPIDER_DEATH, 2.0F, 1.0F);
    
    this._dead = true;
  }
  

  public boolean Update()
  {
    if (this._dead) {
      return true;
    }
    if (UtilTime.elapsed(this._spawn, 15000L)) {
      return true;
    }
    if (this._target == null) {
      return true;
    }
    if (!this._target.GetDriver().isOnline()) {
      return true;
    }
    
    if ((GetEntity() == null) || (!GetEntity().isValid()))
    {
      if (this._spawned) {
        return true;
      }
      SetEntity(GetLocation().getWorld().spawnEntity(GetLocation(), GetType()));
      this._spawned = true;

    }
    else
    {

      Vector dir = UtilAlg.getTrajectory2d(GetEntity(), this._target.GetDriver());
      dir.multiply(0.75D);
      dir.setY(-0.4D);
      GetEntity().setVelocity(dir);
      

      if ((GetEntity() instanceof Creature))
      {
        EntityCreature ec = ((CraftCreature)GetEntity()).getHandle();
        Navigation nav = ec.getNavigation();
        nav.a(this._target.GetDriver().getLocation().getX(), this._target.GetDriver().getLocation().getY(), this._target.GetDriver().getLocation().getZ(), 0.4000000059604645D);
        

        ec.setPositionRotation(GetEntity().getLocation().getX(), GetEntity().getLocation().getY(), GetEntity().getLocation().getZ(), 
          UtilAlg.GetYaw(dir), 0.0F);
      }
    }
    
    return false;
  }
}
