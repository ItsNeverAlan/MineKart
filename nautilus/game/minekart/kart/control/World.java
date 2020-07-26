package nautilus.game.minekart.kart.control;

import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartType;
import nautilus.game.minekart.kart.KartUtil;
import nautilus.game.minekart.kart.crash.Crash;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class World
{
  public static void Gravity(Kart kart)
  {
    Vector vel = kart.GetVelocity();
    if (kart.GetCrash() != null) {
      vel = kart.GetCrash().GetVelocity();
    }
    
    if (KartUtil.IsGrounded(kart))
    {
      if (vel.getY() < 0.0D) {
        vel.setY(0);
      }
      return;
    }
    

    double down = Math.max(-1.0D, vel.getY() - 0.1D);
    vel.setY(down);
  }
  

  public static void AirDrag(Kart kart)
  {
    Vector vel = kart.GetVelocity();
    if (kart.GetCrash() != null) {
      vel = kart.GetCrash().GetVelocity();
    }
    if (vel.length() <= 0.0D) {
      return;
    }
    
    if (KartUtil.IsGrounded(kart))
    {
      double drag = Math.log(vel.length() + 1.0D) / 50.0D;
      drag *= 1.0D / kart.GetKartType().GetTopSpeed();
      drag *= kart.GetKartType().GetAcceleration() / 21.0D;
      

      vel.multiply(1.0D - drag);

    }
    else
    {
      vel.setY(vel.getY() * 0.98D);
    }
  }
  
  public static void FireDrag(Kart kart)
  {
    if (kart.GetDriver().getFireTicks() <= 0) {
      return;
    }
    
    Vector vel = kart.GetVelocity();
    if (kart.GetCrash() != null) {
      return;
    }
    if (vel.length() <= 0.0D) {
      return;
    }
    
    double drag = 0.008D;
    

    vel.multiply(1.0D - drag);
  }
  
  public static void RoadDrag(Kart kart)
  {
    if (!KartUtil.IsGrounded(kart)) {
      return;
    }
    if (kart.GetGP() == null) {
      return;
    }
    if (kart.GetGP().GetTrack() == null) {
      return;
    }
    Block block = kart.GetDriver().getLocation().getBlock().getRelative(org.bukkit.block.BlockFace.DOWN);
    

    Vector vel = kart.GetVelocity();
    if (kart.GetCrash() != null) {
      vel = kart.GetCrash().GetVelocity();
    }
    if (vel.length() <= 0.0D) {
      return;
    }
    
    Vector dragVec = new Vector(vel.getX(), 0.0D, vel.getZ());
    mineplex.core.common.util.UtilAlg.Normalize(dragVec);
    dragVec.multiply(0.003D);
    vel.subtract(dragVec);
    

    if ((!kart.GetGP().GetTrack().GetTrackBlocks().contains(Integer.valueOf(block.getTypeId()))) && (
      (block.getType() == Material.GRASS) || 
      (block.getType() == Material.SAND)))
    {

      double drag = 0.025D;
      

      vel.multiply(1.0D - drag);
      

      if (kart.GetSpeed() > 0.2D) {
        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
      }
    }
    
    if ((!kart.GetDriver().isBlocking()) && 
      (vel.length() < 0.02D)) {
      vel.multiply(0);
    }
  }
  
  public static void BlockDrag(Kart kart) {
    if (!KartUtil.IsGrounded(kart)) {
      return;
    }
    Block block = kart.GetDriver().getLocation().getBlock();
    

    Vector vel = kart.GetVelocity();
    if (kart.GetCrash() != null) {
      vel = kart.GetCrash().GetVelocity();
    }
    if (vel.length() <= 0.0D) {
      return;
    }
    double drag = 0.0D;
    
    if (block.getType() == Material.LONG_GRASS) { drag = 0.02D;
    } else if (block.getType() == Material.WATER) { drag = 0.03D;
    } else if (block.getType() == Material.STATIONARY_WATER) { drag = 0.03D;
    }
    if (drag <= 0.0D) {
      return;
    }
    
    vel.multiply(1.0D - drag);
    

    if (vel.length() > 0.2D) {
      block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
    }
  }
}
