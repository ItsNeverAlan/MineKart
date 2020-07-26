package nautilus.game.minekart.track.ents;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Cow extends TrackEntity
{
  private boolean _spawned = false;
  
  private Kart _owner;
  private Vector _dir;
  private long _spawn = 0L;
  
  public Cow(Track track, Location loc, Kart owner, Vector dir)
  {
    super(track, EntityType.COW, "Stampede", 5.0D, 1.5D, 30000L, loc);
    
    this._owner = owner;
    this._dir = dir;
    
    this._dir.setY(0);
    this._dir.normalize();
    this._dir.multiply(1.2D);
    this._dir.setY(-0.4D);
    
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
    if (UtilMath.offset(kart.GetDriver().getLocation(), GetEntity().getLocation()) > GetCollideRange()) {
      return;
    }
    Collide(kart);
  }
  

  public void Collide(Kart kart)
  {
    if (!kart.IsInvulnerable(false))
    {
      UtilPlayer.message(kart.GetDriver(), F.main("MK", F.elem(this._owner.GetDriver().getName()) + " hit you with " + F.item(GetName()) + "."));
      

      new Crash_Explode(kart, 1.0D, true);
    }
    

    GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.COW_HURT, 2.0F, 1.0F);
  }
  

  public boolean Update()
  {
    if (mineplex.core.common.util.UtilTime.elapsed(this._spawn, 10000L))
    {
      GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.COW_HURT, 2.0F, 0.5F);
      return true;
    }
    
    if ((GetEntity() != null) && (!UtilBlock.airFoliage(GetEntity().getLocation().add(this._dir).add(0.0D, 1.0D, 0.0D).getBlock())))
    {
      GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.COW_HURT, 2.0F, 0.5F);
      return true;
    }
    

    if ((GetEntity() == null) || (!GetEntity().isValid()))
    {
      if (this._spawned)
      {
        GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.COW_HURT, 2.0F, 0.5F);
        return true;
      }
      
      SetEntity(GetLocation().getWorld().spawnEntity(GetLocation(), GetType()));
      this._spawned = true;

    }
    else
    {

      GetEntity().setVelocity(this._dir);
      

      if ((GetEntity() instanceof Creature))
      {
        EntityCreature ec = ((CraftCreature)GetEntity()).getHandle();
        Navigation nav = ec.getNavigation();
        nav.a(GetEntity().getLocation().getX() + this._dir.getX() * 5.0D, GetEntity().getLocation().getY(), GetEntity().getLocation().getZ() + this._dir.getZ() * 5.0D, 0.4000000059604645D);
        

        ec.setPositionRotation(GetEntity().getLocation().getX(), GetEntity().getLocation().getY(), GetEntity().getLocation().getZ(), 
          mineplex.core.common.util.UtilAlg.GetYaw(this._dir), 0.0F);
      }
    }
    
    return false;
  }
}
