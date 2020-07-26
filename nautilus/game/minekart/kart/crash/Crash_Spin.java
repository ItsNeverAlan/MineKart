package nautilus.game.minekart.kart.crash;

import mineplex.core.common.util.UtilAlg;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Crash_Spin extends Crash
{
  public Crash_Spin(Kart kart, Location other, double power)
  {
    super(kart, new Vector(0, 0, 0), (1500.0D * power), true, true);
    
    power = Math.min(power, 1.5D);
    

    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), kart.GetKartType().GetSoundCrash(), 2.0F, 1.0F);
    

    Vector knock = UtilAlg.getTrajectory(other, kart.GetDriver().getLocation());
    knock.setY(0);
    UtilAlg.Normalize(knock);
    knock.multiply(0.6D * power);
    SetVelocity(knock);
    

    kart.CrashStop();
    
    kart.SetKartState(KartState.Crash);
  }
  
  public Crash_Spin(Kart kart, double power)
  {
    super(kart, new Vector(0, 0, 0), (2000.0D * power), true, true);
    
    power = Math.min(power, 1.5D);
    

    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), kart.GetKartType().GetSoundCrash(), 2.0F, 1.0F);
    

    SetVelocity(kart.GetVelocity());
    

    kart.CrashStop();
    
    kart.SetKartState(KartState.Crash);
  }
}
