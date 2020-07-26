package nautilus.game.minekart.track.ents;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.crash.Crash_Explode;
import nautilus.game.minekart.track.Track;
import nautilus.game.minekart.track.TrackEntity;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Mole extends TrackEntity
{
  public Mole(Track track, Location loc)
  {
    super(track, null, "Monty Mole", 5.0D, 1.0D, 1500L, loc);
  }
  

  public void CheckCollision(Kart kart)
  {
    if (kart.GetKartState() == nautilus.game.minekart.kart.KartState.Crash) {
      return;
    }
    if (UtilMath.offset(kart.GetDriver().getLocation(), GetLocation()) > GetCollideRange()) {
      return;
    }
    if (GetLocation().getBlock().getType() != Material.RED_MUSHROOM) {
      return;
    }
    Collide(kart);
  }
  

  public void Collide(Kart kart)
  {
    if (!kart.IsInvulnerable(false))
    {
      UtilPlayer.message(kart.GetDriver(), F.main("MK", "You hit " + F.item(GetName()) + "."));
      

      new Crash_Explode(kart, 1.0D, true);
    }
    

    GetLocation().getWorld().playSound(GetLocation(), Sound.EXPLODE, 2.0F, 1.5F);
    

    GetLocation().getBlock().setType(Material.AIR);
    SetSpawnTimer(System.currentTimeMillis());
  }
  


  public boolean Update()
  {
    if (GetLocation().getBlock().getType() != Material.RED_MUSHROOM)
    {
      if ((Math.random() > 0.99D) && (UtilTime.elapsed(GetSpawnTimer(), GetSpawnRate())))
      {
        GetLocation().getBlock().setType(Material.RED_MUSHROOM);
        SetSpawnTimer(System.currentTimeMillis());
        

        GetLocation().getWorld().playSound(GetLocation(), Sound.BAT_HURT, 0.4F, 1.5F);
        GetLocation().getWorld().playEffect(GetLocation(), Effect.STEP_SOUND, 3);

      }
      

    }
    else if (UtilTime.elapsed(GetSpawnTimer(), 1500L))
    {
      GetLocation().getBlock().setType(Material.AIR);
      SetSpawnTimer(System.currentTimeMillis());
    }
    

    return false;
  }
}
