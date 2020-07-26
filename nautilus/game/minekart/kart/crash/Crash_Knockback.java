package nautilus.game.minekart.kart.crash;

import mineplex.core.common.util.UtilAlg;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Crash_Knockback extends Crash
{
  public Crash_Knockback(Kart kart, Location other, double power)
  {
    super(kart, new Vector(0, 0, 0), 1000L, true, true);
    
    power = Math.min(power, 1.5D);
    

    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), kart.GetKartType().GetSoundCrash(), 2.0F, 1.0F);
    

    Vector knock = UtilAlg.getTrajectory(other, kart.GetDriver().getLocation());
    knock.multiply(0.6D * power);
    knock.add(new Vector(0.0D, power * 0.8D, 0.0D));
    SetVelocity(knock);
    

    kart.CrashStop();
    
    kart.SetKartState(KartState.Crash);
  }
}
