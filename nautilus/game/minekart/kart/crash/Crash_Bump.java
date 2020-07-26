package nautilus.game.minekart.kart.crash;

import mineplex.core.common.util.UtilAlg;
import nautilus.game.minekart.kart.Kart;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Crash_Bump extends Crash
{
  public Crash_Bump(Kart kart, Location other, double power)
  {
    super(kart, new Vector(0, 0, 0), (800.0D * power), true, false);
    

    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.IRONGOLEM_HIT, (float)power, 1.0F);
    

    Vector knock = UtilAlg.getTrajectory(other, kart.GetDriver().getLocation());
    knock.multiply(0.4D * power);
    knock.add(new Vector(0.0D, 0.4D * power, 0.0D));
    SetVelocity(knock);
    
    double powerTrim = Math.min(power, 1.0D) / 2.0D;
    

    kart.GetVelocity().multiply(1.0D - powerTrim);
    
    kart.SetKartState(nautilus.game.minekart.kart.KartState.Crash);
  }
}
