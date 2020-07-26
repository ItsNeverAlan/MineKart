package nautilus.game.minekart.track.ents;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartState;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.TrackEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Silverfish extends TrackEntity
{
  public Silverfish(Track track, Location loc)
  {
    super(track, EntityType.SILVERFISH, "Silverfish", 3.0D, 1.0D, 30000L, loc);
  }
  

  public void Collide(Kart kart)
  {
    if (kart.GetKartState() == KartState.Crash) {
      return;
    }
    if (!kart.IsInvulnerable(false))
    {

      UtilPlayer.message(kart.GetDriver(), F.main("MK", "You hit " + F.item(GetName()) + "."));
      

      if (kart.GetVelocity().length() == 0.0D) {
        kart.SetVelocity(kart.GetDriver().getLocation().getDirection().setY(0));
      }
      kart.SetVelocity(UtilAlg.Normalize(kart.GetVelocityClone().setY(0)).multiply(0.6D));
      new nautilus.game.minekart.kart.crash.Crash_Spin(kart, 0.6000000238418579D);
      kart.SetStability(0);
    }
    

    GetEntity().getWorld().playSound(GetEntity().getLocation(), org.bukkit.Sound.BAT_HURT, 1.0F, 1.0F);
  }
}
