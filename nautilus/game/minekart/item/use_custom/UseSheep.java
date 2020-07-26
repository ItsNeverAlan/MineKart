package nautilus.game.minekart.item.use_custom;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.track.Track;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class UseSheep extends nautilus.game.minekart.item.use_default.ItemUse
{
  public void Use(KartItemManager manager, Kart kart)
  {
    kart.SetItemStored(null);
    
    kart.GetGP().Announce(F.main("MK", F.elem(UtilEnt.getName(kart.GetDriver())) + " used " + F.item("Super Sheep") + "."));
    

    kart.GetGP().GetTrack().GetCreatures().add(
      new nautilus.game.minekart.track.ents.Sheepile(kart.GetGP().GetTrack(), kart.GetDriver().getLocation(), kart));
    

    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.SHEEP_IDLE, 2.0F, 2.0F);
    kart.GetDriver().getWorld().playSound(kart.GetDriver().getLocation(), Sound.SHEEP_IDLE, 2.0F, 2.0F);
  }
}
