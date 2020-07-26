package nautilus.game.minekart.track.ents;

import java.util.ArrayList;
import java.util.HashSet;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.TrackEntity;
import net.minecraft.server.v1_6_R3.EntityCreature;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.util.Vector;

public class Sheepile extends TrackEntity
{
  private boolean _spawned = false;
  private Kart _owner;
  private HashSet<Kart> _hit = new HashSet();
  
  private long _spawn = 0L;
  
  private int _height = 2;
  
  public Sheepile(Track track, Location loc, Kart owner)
  {
    super(track, org.bukkit.entity.EntityType.SHEEP, "Super Sheep", 5.0D, 1.0D, 30000L, loc);
    
    this._owner = owner;
    
    this._spawn = System.currentTimeMillis();
  }
  

  public void CheckCollision(Kart kart)
  {
    if (kart.equals(this._owner)) {
      return;
    }
    if (this._hit.contains(kart)) {
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
    if (!kart.IsInvulnerable(true))
    {
      UtilPlayer.message(kart.GetDriver(), F.main("MK", F.elem(this._owner.GetDriver().getName()) + " hit you with " + F.item(GetName()) + "."));
      UtilPlayer.message(this._owner.GetDriver(), F.main("MK", "You hit " + F.elem(kart.GetDriver().getName()) + " with " + F.item(GetName()) + "."));
      

      new nautilus.game.minekart.kart.crash.Crash_Explode(kart, 1.200000047683716D, true);
    }
    
    this._hit.add(kart);
    

    GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.EXPLODE, 1.0F, 2.0F);
  }
  

  public boolean Update()
  {
    if (mineplex.core.common.util.UtilTime.elapsed(this._spawn, 15000L)) {
      return true;
    }
    
    if ((GetEntity() == null) || (!GetEntity().isValid()))
    {
      if (this._spawned) {
        return true;
      }
      SetEntity(GetLocation().getWorld().spawnEntity(GetLocation(), GetType()));
      this._spawned = true;
      

      Sheep sheep = (Sheep)GetEntity();
      sheep.setBaby();
      sheep.setColor(DyeColor.RED);
      
      return false;
    }
    


    Location target = null;
    

    for (Kart kart : this.Track.GetGP().GetKarts())
    {
      if ((!this._owner.equals(kart)) && (!this._hit.contains(kart)))
      {

        if (UtilMath.offset(kart.GetDriver(), GetEntity()) < 8.0D) {
          target = kart.GetDriver().getLocation();
        }
      }
    }
    if (target == null)
    {
      int best = GetClosestNode();
      Location node = (Location)this.Track.GetProgress().get((best + 1) % this.Track.GetProgress().size());
      
      target = new Location(node.getWorld(), node.getX(), node.getY() + this._height, node.getZ());
    }
    

    Vector dir = UtilAlg.getTrajectory(GetEntity().getLocation(), target);
    dir.normalize();
    dir.multiply(1.2D);
    

    GetEntity().setVelocity(dir);
    

    if ((GetEntity() instanceof Creature))
    {
      EntityCreature ec = ((CraftCreature)GetEntity()).getHandle();
      ec.setPositionRotation(GetEntity().getLocation().getX(), GetEntity().getLocation().getY(), GetEntity().getLocation().getZ(), 
        UtilAlg.GetYaw(dir), 0.0F);
    }
    

    Sheep sheep = (Sheep)GetEntity();
    sheep.setBaby();
    double r = Math.random();
    if (r > 0.75D) { sheep.setColor(DyeColor.RED);
    } else if (r > 0.5D) { sheep.setColor(DyeColor.YELLOW);
    } else if (r > 0.25D) sheep.setColor(DyeColor.GREEN); else {
      sheep.setColor(DyeColor.BLUE);
    }
    
    GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.SHEEP_IDLE, 2.0F, 2.0F);
    
    return false;
  }
  
  public int GetClosestNode()
  {
    int node = -1;
    double bestDist = 9999.0D;
    
    for (int i = 0; i < this.Track.GetProgress().size(); i++)
    {
      Location cur = (Location)this.Track.GetProgress().get(i);
      
      double dist = UtilMath.offset(GetEntity().getLocation().subtract(0.0D, this._height, 0.0D), cur);
      
      if (node == -1)
      {
        node = i;
        bestDist = dist;

      }
      else if (dist < bestDist)
      {
        node = i;
        bestDist = dist;
      }
    }
    
    return node;
  }
}
