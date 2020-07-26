package nautilus.game.minekart.kart;

import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilPlayer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class KartUtil
{
  public static boolean IsGrounded(Kart kart)
  {
    if (mineplex.core.common.util.UtilEnt.isGrounded(kart.GetDriver())) {
      return true;
    }
    
    int ground = kart.GetDriver().getLocation().subtract(0.0D, 0.1D, 0.0D).getBlock().getTypeId();
    if ((ground == 0) || 
      (ground == 8) || 
      (ground == 9) || 
      (ground == 10) || 
      (ground == 11))
    {
      return false;
    }
    
    return kart.GetDriver().getLocation().getY() % 0.5D == 0.0D;
  }
  
  public static boolean Stability(Kart kart, int amount)
  {
    UtilPlayer.hunger(kart.GetDriver(), -amount);
    
    return kart.GetDriver().getFoodLevel() <= 0;
  }
  
  public static Location GetBehind(Kart kart)
  {
    Location loc = kart.GetDriver().getLocation();
    loc.subtract(UtilAlg.Normalize(kart.GetVelocityClone().setY(0)));
    
    return loc;
  }
  
  public static Location GetInfront(Kart kart)
  {
    Location loc = kart.GetDriver().getLocation();
    loc.add(UtilAlg.Normalize(kart.GetVelocityClone().setY(0)));
    
    return loc;
  }
  
  public static Location GetLook(Kart kart)
  {
    Location loc = kart.GetDriver().getLocation();
    loc.add(UtilAlg.Normalize(kart.GetDriver().getLocation().getDirection().setY(0)));
    
    return loc;
  }
  
  public static Vector GetSide(Kart kart)
  {
    Vector look = kart.GetDriver().getLocation().getDirection();
    return new Vector(look.getZ() * -1.0D, 0.0D, look.getX());
  }
}
