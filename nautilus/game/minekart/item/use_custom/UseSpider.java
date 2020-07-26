package nautilus.game.minekart.item.use_custom;

import mineplex.core.common.util.F;
import nautilus.game.minekart.gp.GP;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.item.use_default.ItemUse;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.track.ents.Spiderling;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class UseSpider extends ItemUse
{
  public void Use(KartItemManager manager, Kart kart)
  {
    kart.SetItemStored(null);
    
    kart.GetGP().Announce(F.main("MK", F.elem(mineplex.core.common.util.UtilEnt.getName(kart.GetDriver())) + " used " + F.item("Spiderlings") + "."));
    
    final Kart fKart = kart;
    
    int i = 0;
    for (Kart other : kart.GetGP().GetKarts())
    {
      if (!other.equals(kart))
      {

        final Kart fOther = other;
        
        manager.GetPlugin().getServer().getScheduler().scheduleSyncDelayedTask(manager.GetPlugin(), new Runnable()
        {

          public void run()
          {
            fKart.GetGP().GetTrack().GetCreatures().add(
              new Spiderling(fKart.GetGP().GetTrack(), fKart.GetDriver().getLocation(), fKart, fOther));
            

            fKart.GetDriver().getWorld().playSound(fKart.GetDriver().getLocation(), org.bukkit.Sound.SPIDER_IDLE, 2.0F, 2.0F);
          }
        }, i * 3);
        
        i++;
      }
    }
  }
}
