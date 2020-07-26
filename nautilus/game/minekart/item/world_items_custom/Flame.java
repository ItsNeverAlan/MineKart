package nautilus.game.minekart.item.world_items_custom;

import nautilus.game.minekart.item.KartItemEntity;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.kart.Kart;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Flame extends KartItemEntity
{
  public Flame(KartItemManager manager, Kart kart, Location loc)
  {
    super(manager, kart, loc, Material.FIRE, (byte)0);
    
    SetRadius(1.0D);
    
    SetFired();
  }
  

  public void CollideHandle(Kart kart)
  {
    if (!kart.IsInvulnerable(false))
    {
      int ticks = Math.min(100, kart.GetDriver().getFireTicks() + 20);
      kart.GetDriver().setFireTicks(ticks);
    }
  }
  

  public boolean TickUpdate()
  {
    return System.currentTimeMillis() - GetFireTime() > 6000L;
  }
}
