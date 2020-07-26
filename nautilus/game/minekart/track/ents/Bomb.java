package nautilus.game.minekart.track.ents;

import mineplex.core.common.util.F;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.crash.Crash_Explode;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.TrackEntity;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class Bomb extends TrackEntity
{
  public Bomb(Track track, Location loc)
  {
    super(track, EntityType.CREEPER, "Bomb", 5.0D, 1.0D, 30000L, loc);
  }
  

  public void Collide(Kart kart)
  {
    SetSpawnTimer(System.currentTimeMillis());
    GetEntity().remove();
    
    if (!kart.IsInvulnerable(true))
    {

      mineplex.core.common.util.UtilPlayer.message(kart.GetDriver(), F.main("MK", "You hit " + F.item(GetName()) + "."));
      

      kart.CrashStop();
      new Crash_Explode(kart, 1.200000047683716D, false);
    }
    

    GetEntity().getWorld().playSound(GetEntity().getLocation(), Sound.EXPLODE, 2.0F, 0.2F);
  }
}
