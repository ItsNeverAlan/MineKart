package nautilus.game.minekart.item.world_items_default;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import nautilus.game.minekart.item.KartItemEntity;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.Kart;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Banana extends KartItemEntity
{
  public Banana(KartItemManager manager, Kart kart, Location loc)
  {
    super(manager, kart, loc, Material.INK_SACK, (byte)11);
    
    SetRadius(1.5D);
  }
  

  public void CollideHandle(Kart kart)
  {
    if (!kart.IsInvulnerable(true))
    {

      if (kart.equals(GetOwner()))
      {
        UtilPlayer.message(kart.GetDriver(), F.main("MK", "You hit yourself with " + F.item("Banana") + "."));
      }
      else
      {
        UtilPlayer.message(kart.GetDriver(), F.main("MK", F.elem(UtilEnt.getName(GetOwner().GetDriver())) + " hit you with " + F.item("Banana") + "."));
        UtilPlayer.message(GetOwner().GetDriver(), F.main("MK", "You hit " + F.elem(UtilEnt.getName(kart.GetDriver())) + " with " + F.item("Banana") + "."));
      }
      

      if (kart.GetVelocity().length() == 0.0D) {
        kart.SetVelocity(kart.GetDriver().getLocation().getDirection().setY(0));
      }
      kart.SetVelocity(mineplex.core.common.util.UtilAlg.Normalize(kart.GetVelocityClone().setY(0)).multiply(0.6D));
      new nautilus.game.minekart.kart.crash.Crash_Spin(kart, 0.6000000238418579D);
      kart.SetStability(0);
    }
    

    GetEntity().getWorld().playSound(GetEntity().getLocation(), org.bukkit.Sound.BAT_HURT, 1.0F, 1.0F);
  }
}
