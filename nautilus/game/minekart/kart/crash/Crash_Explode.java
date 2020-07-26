package nautilus.game.minekart.kart.crash;

import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartState;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Crash_Explode extends Crash
{
  public Crash_Explode(Kart kart, double power, boolean resetVelocity)
  {
    super(kart, new Vector(0, 0, 0), (1400.0D * power), true, true);
    

    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), kart.GetKartType().GetSoundCrash(), 2.0F, 1.0F);
    

    SetVelocity(kart.GetVelocityClone().add(new Vector(0.0D, power * 1.0D, 0.0D)));
    

    if (resetVelocity) {
      kart.CrashStop();
    }
    kart.SetKartState(KartState.Crash);
  }
}
