package nautilus.game.minekart.track.ents;

import java.util.ArrayList;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartState;
import nautilus.game.minekart.kart.crash.Crash_Explode;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.TrackEntity;
import net.minecraft.server.v1_6_R3.EntityCreature;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class Train extends TrackEntity
{
  private Location _next = null;
  private Location _past = null;
  
  public Train(Track track, Location loc)
  {
    super(track, EntityType.IRON_GOLEM, "Golem Train", 5.0D, 1.6D, 200L, loc);
    
    this._past = new Location(GetLocation().getWorld(), GetLocation().getX(), GetLocation().getY(), GetLocation().getZ());
    this._next = this._past;
  }
  

  public void Collide(Kart kart)
  {
    if (kart.GetKartState() == KartState.Crash) {
      return;
    }
    if (!kart.IsInvulnerable(false))
    {

      UtilPlayer.message(kart.GetDriver(), F.main("MK", "You hit " + F.item(GetName()) + "."));
      

      kart.CrashStop();
      new Crash_Explode(kart, 1.399999976158142D, false);
    }
    

    GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.EXPLODE, 2.0F, 0.2F);
  }
  












































  public void Movement()
  {
    if (this._next.equals(this._past))
    {

      Location loc = FindTarget();
      
      if (loc == null) {
        return;
      }
      this._next = loc;
      
      if (this._next.getBlock().getRelative(BlockFace.NORTH).equals(this._past.getBlock()))
      {
        this._past = this._next;
        this._next = this._next.getBlock().getRelative(BlockFace.WEST).getLocation().add(0.5D, 0.0D, 0.5D);
      }
      else if (this._next.getBlock().getRelative(BlockFace.SOUTH).equals(this._past.getBlock()))
      {
        this._past = this._next;
        this._next = this._next.getBlock().getRelative(BlockFace.EAST).getLocation().add(0.5D, 0.0D, 0.5D);
      }
      else if (this._next.getBlock().getRelative(BlockFace.EAST).equals(this._past.getBlock()))
      {
        this._past = this._next;
        this._next = this._next.getBlock().getRelative(BlockFace.NORTH).getLocation().add(0.5D, 0.0D, 0.5D);
      }
      else if (this._next.getBlock().getRelative(BlockFace.WEST).equals(this._past.getBlock()))
      {
        this._past = this._next;
        this._next = this._next.getBlock().getRelative(BlockFace.SOUTH).getLocation().add(0.5D, 0.0D, 0.5D);
      }
    }
    else
    {
      Location loc = FindTarget();
      
      if (loc == null) {
        return;
      }
      this._past = this._next;
      this._next = loc;
      
      GetEntity().teleport(this._next);
      SetSpawnTimer(System.currentTimeMillis());
      
      if ((GetEntity() instanceof Creature))
      {
        EntityCreature ec = ((CraftCreature)GetEntity()).getHandle();
        

        ec.setPositionRotation(GetEntity().getLocation().getX(), GetEntity().getLocation().getY(), GetEntity().getLocation().getZ(), 
          UtilAlg.GetYaw(UtilAlg.getTrajectory2d(this._past, this._next)), 0.0F);
      }
    }
  }
  





























  public Location FindTarget()
  {
    ArrayList<Block> tracks = new ArrayList();
    

    Block check = this._next.getBlock().getRelative(0, 0, 1);
    if ((check.getTypeId() == 66) || (check.getTypeId() == 27)) { tracks.add(check);
    }
    check = this._next.getBlock().getRelative(1, 0, 0);
    if ((check.getTypeId() == 66) || (check.getTypeId() == 27)) { tracks.add(check);
    }
    check = this._next.getBlock().getRelative(0, 0, -1);
    if ((check.getTypeId() == 66) || (check.getTypeId() == 27)) { tracks.add(check);
    }
    check = this._next.getBlock().getRelative(-1, 0, 0);
    if ((check.getTypeId() == 66) || (check.getTypeId() == 27)) { tracks.add(check);
    }
    tracks.remove(this._past.getBlock());
    
    if (tracks.isEmpty()) {
      return null;
    }
    return ((Block)tracks.get(0)).getLocation().add(0.5D, 0.0D, 0.5D);
  }
}
